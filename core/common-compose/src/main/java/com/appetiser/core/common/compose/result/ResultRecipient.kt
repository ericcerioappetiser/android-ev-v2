package com.appetiser.core.common.compose.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.Stable


@Stable
interface ResultRecipient<D, R> {

    /**
     * Install a [listener] that will be called when the [D] destination
     * finishes. [NavResult] will either contain a specific result [R] for [NavResult.Value]
     * or it will be [NavResult.Canceled] if [D] finishes without setting any result.
     *
     * Implementation makes sure to only do something the first time it is called,
     * so, no need to worry about recomposition.
     *
     * [listener] will not be called from a Compose scope, it should be treated
     * as a normal button click listener, you can navigate or call a method on a view model,
     * for example.
     */
    @Composable
    fun NavResultEffect(listener: @DisallowComposableCalls (NavResult<R>) -> Unit)
}
