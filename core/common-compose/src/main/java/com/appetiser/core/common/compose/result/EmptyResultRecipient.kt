package com.appetiser.core.common.compose.result

import androidx.compose.runtime.Composable

/**
 * Empty implementation of [ResultRecipient] to
 * use in previews and possibly testing.
 */
class EmptyResultRecipient<D : Any, R> : ResultRecipient<D, R> {

    @Composable
    override fun NavResultEffect(listener: (NavResult<R>) -> Unit) = Unit
}
