package com.appetiser.feature.profile.ui.accountsettings.newpassword

import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_INCORRECT_PASSWORD = 401

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : BaseViewModel<NewPasswordViewModel.ViewState, NewPasswordViewModel.Action>() {

    override val initialState: ViewState
        get() = ViewState()

    init {
        onAction {
            handleAction(it)
        }
    }

    override suspend fun onInitial() {
        val user = userRepository.getCurrentUser()
        modifyState {
            copy(email = user.email)
        }
    }

    private suspend fun handleAction(action: Action) {
        when (action) {
            is Action.ShowPreviousPage -> showPreviousPage()
            is Action.ChangeCurrentPassword -> changeCurrentPassword(action.password)
            is Action.SubmitCurrentPassword -> submitCurrentPassword()
            is Action.ChangeNewPassword -> changeNewPassword(action.password)
            is Action.SubmitNewPassword -> submitNewPassword()
        }
    }

    private suspend fun showPreviousPage() {
        modifyState {
            copy(page = NewPasswordPage.YOUR_PASSWORD)
        }
    }

    private suspend fun changeCurrentPassword(password: String) {
        modifyState {
            copy(
                currentPassword = password,
                hasCurrentPasswordError = false
            )
        }
    }

    private suspend fun submitCurrentPassword() {
        try {
            authRepository.verifyPassword(state.currentPassword)
            modifyState {
                copy(page = NewPasswordPage.NEW_PASSWORD)
            }
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_INCORRECT_PASSWORD) {
                modifyState {
                    copy(hasCurrentPasswordError = true)
                }
            }
        }
    }

    private suspend fun changeNewPassword(password: String) {
        modifyState {
            copy(newPassword = password)
        }
    }

    private suspend fun submitNewPassword() {
        authRepository.changePassword(
            oldPassword = state.currentPassword,
            newPassword = state.newPassword,
            newPasswordConfirmation = state.newPassword
        )
        modifyState {
            copy(passwordUpdated = Consumable(true))
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
        val page: NewPasswordPage = NewPasswordPage.YOUR_PASSWORD,
        val email: String = "",
        val currentPassword: String = "",
        val hasCurrentPasswordError: Boolean = false,
        val newPassword: String = "",
        val passwordUpdated: Consumable<Boolean>? = null,
        val error: ConsumableError? = null
    ) {

        val currentPasswordValid = currentPassword.isNotBlank() && hasCurrentPasswordError.not()
    }

    sealed interface Action {

        data object ShowPreviousPage : Action

        data class ChangeCurrentPassword(
            val password: String
        ) : Action

        data object SubmitCurrentPassword : Action

        data class ChangeNewPassword(
            val password: String
        ) : Action

        data object SubmitNewPassword : Action
    }
}
