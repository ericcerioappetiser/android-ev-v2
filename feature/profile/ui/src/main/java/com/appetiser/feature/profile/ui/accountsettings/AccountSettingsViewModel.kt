package com.appetiser.feature.profile.ui.accountsettings

import androidx.lifecycle.viewModelScope
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.domain.user.UsernameType
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : BaseViewModel<AccountSettingsViewModel.ViewState, AccountSettingsViewModel.ActionState>() {

    override val initialState: ViewState
        get() = ViewState()

    init {
        onAction {
            handleAction(it)
        }
    }

    override suspend fun onInitial() {
        viewModelScope.launchWithErrorHandling {
            userRepository
                .getCurrentUserFlow()
                .collect { user ->
                    modifyState {
                        copy(
                            username = when (user.primaryUsername) {
                                UsernameType.EMAIL -> user.email
                                UsernameType.PHONE_NUMBER -> user.phoneNumber
                                else -> ""
                            },
                            usernameType = user.primaryUsername
                        )
                    }
                }
        }
    }

    private suspend fun handleAction(actionState: ActionState) {
        when (actionState) {
            is ActionState.LogoutCurrentUser -> {
                authRepository.logout()
            }
        }
    }

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState {
            copy(error = ConsumableError(error))
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val username: String = "",
        val usernameType: UsernameType = UsernameType.UNKNOWN,
        val error: ConsumableError? = null
    )

    sealed interface ActionState {

        data object LogoutCurrentUser : ActionState
    }
}
