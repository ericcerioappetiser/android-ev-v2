package com.appetiser.androidecv.screens.main.home

import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeViewModel.ViewState, HomeViewModel.Action>() {

    data class ViewState(
        val text: String,
        val count: Int,
        val oneTime: ConsumableUnit?
    )

    sealed interface Action {
        data class Type(val text: String) : Action
        data object Increment : Action
        data object OneTime : Action
    }

    override suspend fun onInitial() {
        super.onInitial()
        onAction {
            when (it) {
                Action.Increment -> modifyState {
                    copy(count = count + 1)
                }

                Action.OneTime -> {
                    modifyState {
                        copy(oneTime = ConsumableUnit())
                    }
                }

                is Action.Type -> {
                    modifyState {
                        copy(text = it.text)
                    }
                }
            }
        }
    }

    override val initialState: ViewState
        get() = ViewState(
            text = "",
            count = 0,
            oneTime = null
        )

    override suspend fun errorHandler(error: Throwable) {
        Timber.d(error)
    }
}
