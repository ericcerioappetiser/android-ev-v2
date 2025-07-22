package com.appetiser.core.common.compose.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import kotlin.reflect.KClass

internal class ResultRecipientImpl<D : Any, R : Any>(
    private val navBackStackEntry: NavBackStackEntry,
    resultOriginType: KClass<D>,
    resultNavType: KClass<R>,
) : ResultRecipient<D, R> {

    private val savedStateHandle = navBackStackEntry.savedStateHandle

    private val resultKey = resultKey(resultOriginType, resultNavType)
    private val canceledKey = canceledKey(resultOriginType, resultNavType)

    @Composable
    override fun NavResultEffect(listener: (NavResult<R>) -> Unit) {
        val currentListener by rememberUpdatedState(listener)

        DisposableEffect(navBackStackEntry) {
            val observer = object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_START,
                        Lifecycle.Event.ON_RESUME -> {
                            handleResultIfPresent(currentListener)
                        }

                        Lifecycle.Event.ON_DESTROY -> {
                            navBackStackEntry.lifecycle.removeObserver(this)
                        }

                        else -> Unit
                    }
                }
            }

            navBackStackEntry.lifecycle.addObserver(observer)

            onDispose {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        }
    }

    private fun handleResultIfPresent(listener: (NavResult<R>) -> Unit) {
        if (!hasAnyResult()) {
            return
        }

        val canceled = savedStateHandle.remove<Boolean>(canceledKey)

        if (canceled == true) {
            listener(NavResult.Canceled)
        } else if (savedStateHandle.contains(resultKey)) {
            @Suppress("UNCHECKED_CAST")
            val result = savedStateHandle.remove<Any?>(resultKey) as R
            listener(NavResult.Value(result))
        }
    }

    private fun hasAnyResult(): Boolean = savedStateHandle.contains(canceledKey) ||
        savedStateHandle.contains(resultKey)
}
