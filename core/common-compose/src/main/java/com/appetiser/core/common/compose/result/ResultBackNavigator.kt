package com.appetiser.core.common.compose.result

import androidx.compose.runtime.Stable


@Stable
interface ResultBackNavigator<D : Any, R : Any> {

    fun navigateBack(result: R)
    fun setResult(result: R)
    fun navigateBack()
}
