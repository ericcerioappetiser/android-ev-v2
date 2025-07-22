package com.appetiser.feature.profile.ui.accountsettings.newemail

import androidx.core.util.PatternsCompat
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_INVALID_CODE = 400
private const val ERROR_INCORRECT_PASSWORD = 401
private const val ERROR_DUPLICATE_EMAIL = 422

@HiltViewModel
class NewEmailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : BaseViewModel<NewEmailViewModel.ViewState, NewEmailViewModel.Action>() {

    private var token = ""

    override val initialState: ViewState
        get() = ViewState()

    init {
        onAction {
            handleAction(it)
        }
    }

    private suspend fun handleAction(action: Action) {
        when (action) {
            is Action.ShowPreviousPage -> showPreviousPage()
            is Action.ChangePassword -> changePassword(action.password)
            is Action.ForgotPassword -> forgotPassword()
            is Action.SubmitPassword -> submitPassword()
            is Action.ChangeEmail -> changeEmail(action.email)
            is Action.SubmitEmail -> submitEmail()
            is Action.ChangeCode -> changeCode(action.index, action.char)
            is Action.SubmitCode -> submitCode()
        }
    }

    private suspend fun showPreviousPage() {
        modifyState {
            copy(
                page = if (state.page == NewEmailPage.VERIFY_EMAIL) {
                    NewEmailPage.NEW_EMAIL
                } else {
                    NewEmailPage.YOUR_PASSWORD
                }
            )
        }
    }

    private suspend fun changePassword(password: String) {
        modifyState {
            copy(
                password = password,
                hasPasswordError = false
            )
        }
    }

    private suspend fun forgotPassword() {
        val user = userRepository.getCurrentUser()
        modifyState {
            copy(passwordForgot = Consumable(user.email))
        }
    }

    private suspend fun submitPassword() {
        try {
            token = authRepository.verifyPassword(state.password)
            modifyState {
                copy(page = NewEmailPage.NEW_EMAIL)
            }
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_INCORRECT_PASSWORD) {
                modifyState {
                    copy(hasPasswordError = true)
                }
            }
        }
    }

    private suspend fun changeEmail(email: String) {
        modifyState {
            copy(
                email = email,
                hasEmailError = false
            )
        }
    }

    private suspend fun submitEmail() {
        try {
            authRepository.requestChangeEmail(
                verificationToken = token,
                newEmail = state.email
            )
            modifyState {
                copy(page = NewEmailPage.VERIFY_EMAIL)
            }
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_DUPLICATE_EMAIL) {
                modifyState {
                    copy(hasEmailError = true)
                }
            }
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
            authRepository.verifyChangeEmail(
                verificationToken = token,
                code = state.code.joinToString("")
            )
            modifyState {
                copy(emailUpdated = Consumable(true))
            }
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_INVALID_CODE) {
                modifyState {
                    copy(hasCodeError = true)
                }
            }
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
        val page: NewEmailPage = NewEmailPage.YOUR_PASSWORD,
        val password: String = "",
        val passwordForgot: Consumable<String>? = null,
        val hasPasswordError: Boolean = false,
        val email: String = "",
        val hasEmailError: Boolean = false,
        val code: List<String> = listOf("", "", "", "", ""),
        val hasCodeError: Boolean = false,
        val emailUpdated: Consumable<Boolean>? = null,
        val error: ConsumableError? = null
    ) {

        val passwordValid = password.isNotBlank() && hasPasswordError.not()
        val emailValid = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() && hasEmailError.not()
    }

    sealed interface Action {

        data object ShowPreviousPage : Action

        data class ChangePassword(
            val password: String
        ) : Action

        data object ForgotPassword : Action

        data object SubmitPassword : Action

        data class ChangeEmail(
            val email: String
        ) : Action

        data object SubmitEmail : Action

        data class ChangeCode(
            val index: Int,
            val char: String
        ) : Action

        data object SubmitCode : Action
    }
}
