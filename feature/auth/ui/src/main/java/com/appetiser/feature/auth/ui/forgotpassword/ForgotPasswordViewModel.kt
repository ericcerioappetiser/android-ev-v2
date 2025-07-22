package com.appetiser.feature.auth.ui.forgotpassword

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.auth.ui.ForgotPasswordRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_INVALID_CODE = 422

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel<ForgotPasswordViewModel.ViewState, ForgotPasswordViewModel.Action>() {

    private val route = savedStateHandle.toRoute<ForgotPasswordRoute>()

    override val initialState: ViewState
        get() = ViewState(email = route.email)

    init {
        onAction {
            handleAction(it)
        }
    }

    override suspend fun onInitial() {
        sendCode(route.email)
    }

    private suspend fun sendCode(email: String) {
        authRepository.requestResetPassword(email)
    }

    private suspend fun handleAction(action: Action) {
        when (action) {
            is Action.ShowPreviousPage -> showPreviousPage()
            is Action.ChangeCode -> changeCode(action.index, action.char)
            is Action.SubmitCode -> submitCode()
            is Action.ResendCode -> sendCode(state.email)
            is Action.ChangePassword -> changePassword(action.password)
            is Action.SubmitPassword -> submitPassword()
        }
    }

    private suspend fun showPreviousPage() {
        modifyState {
            copy(page = ForgotPasswordPage.VERIFY_EMAIL)
        }
    }

    private suspend fun changeCode(index: Int, char: String) {
        modifyState {
            copy(
                code = state.code.toMutableList().also {
                    it[index] = char
                },
                hasCodeError = false
            )
        }
    }

    private suspend fun submitCode() {
        try {
            authRepository.checkResetPassword(
                username = state.email,
                token = state.code.joinToString("")
            )
            modifyState {
                copy(page = ForgotPasswordPage.NEW_PASSWORD)
            }
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_INVALID_CODE) {
                modifyState {
                    copy(hasCodeError = true)
                }
            }
        }
    }

    private suspend fun changePassword(password: String) {
        modifyState {
            copy(password = password)
        }
    }

    private suspend fun submitPassword() {
        authRepository.resetPassword(
            username = state.email,
            token = state.code.joinToString(""),
            password = state.password,
            passwordConfirmation = state.password
        )
        modifyState {
            copy(passwordUpdated = Consumable(true))
        }
    }

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState {
            copy(error = Consumable(error))
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val page: ForgotPasswordPage = ForgotPasswordPage.VERIFY_EMAIL,
        val email: String = "",
        val code: List<String> = listOf("", "", "", "", ""),
        val hasCodeError: Boolean = false,
        val password: String = "",
        val passwordUpdated: Consumable<Boolean>? = null,
        val error: ConsumableError? = null
    )

    sealed interface Action {

        data object ShowPreviousPage : Action

        data class ChangeCode(
            val index: Int,
            val char: String
        ) : Action

        data object SubmitCode : Action

        data object ResendCode : Action

        data class ChangePassword(
            val password: String
        ) : Action

        data object SubmitPassword : Action
    }
}
