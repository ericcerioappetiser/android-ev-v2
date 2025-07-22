package com.appetiser.feature.profile.ui.about

import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor() : BaseViewModel<AboutViewModel.ViewState, AboutViewModel.Action>() {

    override val initialState: ViewState
        get() = ViewState()

    init {
        onAction {
            handleAction(it)
        }
    }

    private suspend fun handleAction(action: Action) {}

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState {
            copy(error = ConsumableError(error))
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val error: ConsumableError? = null
    )

    sealed interface Action
}
