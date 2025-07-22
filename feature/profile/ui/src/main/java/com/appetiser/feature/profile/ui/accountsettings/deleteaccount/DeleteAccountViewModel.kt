package com.appetiser.feature.profile.ui.accountsettings.deleteaccount

import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.domain.user.UsernameType
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_INVALID_CREDENTIALS = 401

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel<DeleteAccountViewModel.ViewState, DeleteAccountViewModel.Action>() {

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
            copy(
                usernameType = user.primaryUsername,
                email = user.email,
                phone = user.phoneNumber
            )
        }

        if (user.primaryUsername == UsernameType.PHONE_NUMBER) {
            sendCode()
        }
    }

    private suspend fun handleAction(action: Action) {
        when (action) {
            is Action.ChangePassword -> changePassword(action.password)
            is Action.SubmitPassword -> submitPassword()
            is Action.ChangeCode -> changeCode(action.index, action.char)
            is Action.SubmitCode -> submitCode()
            is Action.ResendCode -> sendCode()
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

    private suspend fun submitPassword() {
        try {
            val token = authRepository.verifyPassword(state.password)
            authRepository.deleteAccount(token)
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_INVALID_CREDENTIALS) {
                modifyState {
                    copy(hasPasswordError = true)
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
            val token = authRepository.verifyOtp(
                otp = state.code.joinToString("")
            )
            authRepository.deleteAccount(token)
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_INVALID_CREDENTIALS) {
                modifyState {
                    copy(hasCodeError = true)
                }
            }
        }
    }

    private suspend fun sendCode() {
        authRepository.generateOtp(state.phone)
    }

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState {
            copy(error = ConsumableError(error))
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val usernameType: UsernameType = UsernameType.UNKNOWN,
        val email: String = "",
        val password: String = "",
        val hasPasswordError: Boolean = false,
        val phone: String = "",
        val code: List<String> = listOf("", "", "", "", ""),
        val hasCodeError: Boolean = false,
        val accountDeleted: Consumable<Boolean>? = null,
        val error: ConsumableError? = null
    ) {

        val passwordValid = password.isNotBlank() && hasPasswordError.not()
    }

    sealed interface Action {

        data class ChangePassword(
            val password: String
        ) : Action

        data object SubmitPassword : Action

        data class ChangeCode(
            val index: Int,
            val char: String
        ) : Action

        data object SubmitCode : Action

        data object ResendCode : Action
    }
}
