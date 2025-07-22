package com.appetiser.core.common.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appetiser.core.common.android.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/**
 * Consume a [BaseViewModel] [BaseViewModel.Consumable] as a side effect
 */
@Composable
fun <T> ConsumableSideEffect(
    consumable: BaseViewModel.Consumable<T>?,
    effect: suspend (T) -> Unit
) {
    val updatedConsumable by rememberUpdatedState(consumable)
    LaunchedEffect(Unit) {
        snapshotFlow { updatedConsumable }
            .map { it?.consume() }
            .filterNotNull()
            .collectLatest { value: T ->
                // collectLatest will cancel the previous effect
                effect(value)
            }
    }
}

/**
 * Collect a [BaseViewModel] state as a [State] scoped with lifecycle
 */
@Composable
fun <S : Any> BaseViewModel<S, *>.collectAsState(): State<S> = stateFlow.collectAsStateWithLifecycle()
