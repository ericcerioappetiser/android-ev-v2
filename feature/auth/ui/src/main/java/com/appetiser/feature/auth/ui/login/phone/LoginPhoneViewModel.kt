package com.appetiser.feature.auth.ui.login.phone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.appetiser.baseplate.common.util.COUNTDOWN_MAX_TIMER
import com.appetiser.baseplate.common.util.DEFAULT_DELAY_IN_MILLISECONDS
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.common.android.base.ConsumableUnit
import com.appetiser.core.common.android.util.launchWithTimber
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.auth.ui.LoginViaPhoneRoute
import com.appetiser.feature.auth.ui.login.phone.LoginPhoneViewModel.ActionState
import com.appetiser.feature.auth.ui.login.phone.LoginPhoneViewModel.LoginPhoneViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginPhoneViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel<LoginPhoneViewState, ActionState>() {

    private val phoneNumber = savedStateHandle.toRoute<LoginViaPhoneRoute>().phoneNumber
    private var resendTimerJob: Job? = null

    init {
        onAction { handleAction(it) }
    }

    override val initialState: LoginPhoneViewState
        get() = LoginPhoneViewState(
            phoneNumber = phoneNumber
        )

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = error.asConsumable()) }
    }


    private suspend fun handleAction(action: ActionState) {
        when (action) {
            is ActionState.OnResendClick -> resendCode()

            is ActionState.OnOtpInput -> doOnOtpInput(action)

            is ActionState.OnOtpComplete -> verifyLoginPhone()
        }
    }

    private suspend fun doOnOtpInput(
        action: ActionState.OnOtpInput,
    ) {
        modifyState {
            val newOtp = otp.toMutableList()
            newOtp[action.index] = action.otpChar
            copy(otp = newOtp)
        }
    }

    private suspend fun verifyLoginPhone() {
        val phoneNumber = state.phoneNumber
        val otp = state.otp

        modifyState { copy(isLoading = true) }
        authRepository.loginWithPhone(
            "+$phoneNumber",
            otp.joinToString(separator = "")
        )

        modifyState { copy(onLoginSuccess = ConsumableUnit()) }
    }

    private suspend fun resendCode() {
        authRepository.generateOtp(state.phoneNumber)
        startTimer()
    }

    private fun startTimer() {
        resendTimerJob?.cancel()
        resendTimerJob = viewModelScope.launchWithTimber {
            flow {
                for (ticker in COUNTDOWN_MAX_TIMER downTo 1L) {
                    emit(ticker)
                    delay(DEFAULT_DELAY_IN_MILLISECONDS)
                }
            }.onCompletion {
                modifyState { copy(timer = null) }
            }.collect { timeLeft ->
                modifyState { copy(timer = timeLeft.toInt()) }
            }
        }
    }

    data class LoginPhoneViewState(
        val phoneNumber: String = "",
        val otp: List<String> = mutableListOf("", "", "", "", ""),
        val isLoading: Boolean = false,
        val timer: Int? = null,
        val onLoginSuccess: ConsumableUnit? = null,
        val error: ConsumableError? = null
    )

    sealed interface ActionState {
        data object OnResendClick : ActionState

        data class OnOtpInput(val index: Int, val otpChar: String) : ActionState
        data object OnOtpComplete : ActionState
    }
}
