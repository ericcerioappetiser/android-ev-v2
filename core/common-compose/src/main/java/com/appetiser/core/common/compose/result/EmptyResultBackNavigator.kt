package com.appetiser.core.common.compose.result

/**
 * Empty implementation of [ResultBackNavigator] to
 * use in previews and possibly testing.
 */
class EmptyResultBackNavigator<R : Any> : ResultBackNavigator<Any, R> {

    override fun navigateBack(result: R) = Unit

    override fun setResult(result: R) = Unit

    override fun navigateBack() = Unit
}
