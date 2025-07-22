package com.appetiser.core.common.compose.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.reflect.KClass

internal class ResultBackNavigatorImpl<D : Any, R : Any>(
    private val navController: NavController,
    resultOriginType: KClass<D>,
    resultNavType: KClass<R>
) : ResultBackNavigator<D, R> {

    private val resultKey = resultKey(resultOriginType, resultNavType)
    private val canceledKey = canceledKey(resultOriginType, resultNavType)

    override fun navigateBack(result: R) {
        setResult(result)
        navigateBack()
    }

    override fun setResult(result: R) {
        navController.previousBackStackEntry?.savedStateHandle?.let {
            it[canceledKey] = false
            it[resultKey] = result
        }
    }

    override fun navigateBack() {
        navController.navigateUp()
    }

    @Composable
    internal fun HandleCanceledEffect() {
        val currentNavBackStackEntry = navController.currentBackStackEntryAsState().value ?: return

        DisposableEffect(currentNavBackStackEntry) {
            val observer = object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> {
                            val savedStateHandle =
                                navController.previousBackStackEntry?.savedStateHandle ?: return

                            if (!savedStateHandle.contains(canceledKey)) {
                                // We set canceled to true when this destination becomes visible
                                // When a value to be returned is set, we will put the canceled to `false`
                                savedStateHandle[canceledKey] = true
                                currentNavBackStackEntry.lifecycle.removeObserver(this)
                            }
                        }

                        else -> Unit
                    }
                }
            }

            currentNavBackStackEntry.lifecycle.addObserver(observer)

            onDispose {
                currentNavBackStackEntry.lifecycle.removeObserver(observer)
            }
        }
    }
}
