package com.appetiser.core.common.android.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appetiser.core.common.android.util.launchWithTimber
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

typealias ConsumableError = BaseViewModel.Consumable<Throwable>
typealias ConsumableUnit = BaseViewModel.Consumable<Unit>

@Suppress("FunctionName") // Factory function
fun ConsumableUnit() = ConsumableUnit(Unit)


private class WhenValueRead : SharingStarted {

    private val shouldStart = CompletableDeferred<Unit>()

    fun startSharing() {
        shouldStart.complete(Unit)
    }

    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> = channelFlow {
        try {
            select {
                // Listen when
                shouldStart.onJoin {
                    send(SharingCommand.START)
                }

                // Lazily implementation
                launch {
                    subscriptionCount.first { it > 0 }
                    send(SharingCommand.START)
                }
            }
        } finally {
            // we close the channelFlow so that subscriptionCount collector cancels
            close()
        }
    }
}

/**
 * The `BaseStateViewModel` class follows the Unidirectional Data Flow (UDF) pattern.
 * It manages state within the ViewModel itself rather than dispatching events.
 * This approach ensures a single source of truth for the state, making the state management predictable, easier to debug, easier to maintain, and scalable.
 *
 * @param S The type of the View State. This represents the state of the view at any given time.
 * @param A The type of the Action. This represents the actions that can be performed to change the state.
 */
abstract class BaseViewModel<S : Any, A : Any> : ViewModel() {

    /**
     * A [Consumable] is a wrapper class that allows a value to be consumed only once.
     * This is useful for UI events that should only be handled once. For example, showing a snackbar or navigation.
     *
     * Usage:
     * ```
     * // In the ViewModel
     * currentViewState.copy(
     *    navigateToHome = Consumable(true)
     * ).emit()
     *
     * // In the UI
     * viewModel.state.collect { state ->
     *   state.navigateToHome.consume()?.let { navigateToHome ->
     *     if (navigateToHome) {
     *      // Navigate to home
     *     }
     *   }
     * }
     * ```
     *
     * @param T The type of the content that will be a consumable.
     * @param callback An optional callback that will be invoked when the content is consumed.
     */
    class Consumable<T>(content: T, private val callback: (() -> Unit)? = null) {

        private var content: AtomicRef<T?> = atomic(content)

        val isConsumed get() = content.value == null

        /**
         * Consumes the content if it has not been consumed yet.
         *
         * @return The content if it has not been consumed yet, otherwise null.
         */
        fun consume(): T? = content.getAndSet(null).also { callback?.invoke() }

        /**
         * Tries consumes the content if the [predicate] is true.
         * This is useful for consuming the content only if a certain condition is met.
         *
         * Usage:
         * ```
         * consumable.tryConsumeIf { it == true }?.let { content ->
         *  // Handle content
         * }
         * ```
         *
         * @param predicate The predicate to check if the content should be consumed.
         * @return The content if the [predicate] is true and it has not been consumed yet, otherwise null.
         */
        fun tryConsumeIf(predicate: (T) -> Boolean): T? = content.getAndUpdate {
            if (it != null && predicate(it)) {
                callback?.invoke()
                null
            } else {
                it
            }
        }

        /**
         * Peeks the content without consuming it.
         *
         * @return The content if it has not been consumed yet, otherwise null.
         */
        fun peek(): T? = content.value

        override fun toString(): String = "Consumable(content=${peek()})"
    }

    protected val stateMutex: Mutex = Mutex()

    private val mutableState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }

    val action: (A) -> Unit
    private val actionFlow: MutableSharedFlow<A> = MutableSharedFlow()

    /**
     * Initial state value, useful for initially populating values like from [androidx.lifecycle.SavedStateHandle]
     */
    protected abstract val initialState: S

    init {
        action = { action ->
            viewModelScope.launchWithTimber {
                actionFlow.emit(action)
            }
        }
    }

    private val sharingStarted by lazy { WhenValueRead() }

    val stateFlow: StateFlow<S> by lazy {
        mutableState
            .onStart { viewModelScope.launch { onInitial() } }
            .stateIn(viewModelScope, sharingStarted, mutableState.value)
    }

    /**
     * Called to perform initial actions when the ViewModel is created. This can be used to fetch initial data, or
     * to hook up flow collectors
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    open suspend fun onInitial() = Unit

    /**
     * Attaches a [FlowCollector] to the [actionFlow] to listen for actions of type [A].
     * This should be called on the init block
     *
     * @param collectLatest If true, the [actionFlow] will collect the latest value.
     * @param action The [FlowCollector] that will be called when an action is emitted.
     */
    protected fun onAction(
        collectLatest: Boolean = false,
        action: FlowCollector<A>
    ) = onAction(
        mutate = { this },
        action = action,
        collectLatest = collectLatest
    )

    /**
     * Attaches a [FlowCollector] to the [actionFlow] to listen for actions of type [A].
     * This should be called on the init block
     *
     * @param mutate The function that will be called to mutate the [actionFlow].
     * @param collectLatest If true, the [actionFlow] will collect the latest value.
     * @param action The [FlowCollector] that will be called when an action is emitted.
     */
    protected fun <R> onAction(
        mutate: SharedFlow<A>.() -> Flow<R>,
        collectLatest: Boolean = false,
        action: FlowCollector<R>
    ) {
        viewModelScope.launchWithTimber {
            suspend fun actionInternal(item: R) {
                try {
                    action.emit(item)
                } catch (e: Throwable) {
                    if (e is CancellationException) throw e
                    errorHandler(e)
                }
            }
            actionFlow.mutate().let { flow ->
                if (collectLatest) {
                    flow.collectLatest { actionInternal(it) }
                } else {
                    flow.collect { actionInternal(it) }
                }
            }
        }
    }

    /**
     * Atomically modifies the current [S] using the provided [function].
     */
    protected suspend fun modifyState(function: S.() -> S) {
        stateMutex.withLock {
            suspendCancellableCoroutine { continuation ->
                try {
                    mutableState.update {
                        if (continuation.isActive) {
                            function(it)
                        } else {
                            throw CancellationException()
                        }
                    }
                    continuation.resume(Unit)
                } catch (e: Throwable) {
                    if (e is CancellationException) throw e
                    continuation.resumeWithException(e)
                }
            }
        }
    }

    /**
     * Atomically modifies the current [S] by [this] value.
     */
    @JvmName("emitState")
    protected suspend fun S.emit() {
        modifyState { this@emit }
    }

    /**
     * Atomically modifies the current [S] by [this] value.
     *
     * This allow for invoking the [emit] function without the need to call it.
     * Example:
     * ```
     * state.copy(isLoading = true)()
     * ```
     */
    @JvmName("invokeEmitState")
    protected suspend operator fun S.invoke() = emit()

    /**
     * Converts [this] value to a [Consumable].
     */
    protected fun <T> T.asConsumable(callback: (() -> Unit)? = null) = Consumable(this, callback)

    /**
     * Returns the current state of the ViewModel.
     */
    val state: S
        get() {
            sharingStarted.startSharing()
            return stateFlow.value
        }

    /**
     * Launches a coroutine with hook to [errorHandler]
     */
    protected fun CoroutineScope.launchWithErrorHandling(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = launch(errorHandler + context, start, block)

    private val errorHandler by lazy {
        CoroutineExceptionHandler { _, error ->
            viewModelScope.launch { errorHandler(error) }
        }
    }

    /**
     * Handles the unhandled exceptions thrown by the ViewModel.
     */
    protected abstract suspend fun errorHandler(error: Throwable)
}
