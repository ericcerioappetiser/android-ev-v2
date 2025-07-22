package com.appetiser.feature.auth.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.appetiser.baseplate.common.util.COUNTDOWN_MAX_TIMER
import com.appetiser.baseplate.common.util.DEFAULT_DELAY_IN_MILLISECONDS
import com.appetiser.baseplate.common.util.PhoneUtils.isValidPhoneNumber
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableUnit
import com.appetiser.core.common.android.util.ResourceManager
import com.appetiser.core.common.android.util.TimeUtils.formatToMinutesAndSeconds
import com.appetiser.core.common.android.util.launchWithTimber
import com.appetiser.core.common.compose.components.DEFAULT_BP_OTP_LENGTH
import com.appetiser.core.domain.user.User
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.auth.data.SessionRepository
import com.appetiser.feature.auth.ui.VerifyOtpViewModel.Action
import com.appetiser.feature.auth.ui.VerifyOtpViewModel.VerifyOtpViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle,
    private val resourceManager: ResourceManager
) : BaseViewModel<VerifyOtpViewState, Action>() {

    private val argsUserName = savedStateHandle.toRoute<VerifyOtpRoute>().username
    private var timerJob: Job? = null

    init {
        viewModelScope.launchWithErrorHandling {
            updateArgsUsername()
        }
        viewModelScope.launchWithErrorHandling {
            getCurrentUser()
        }
        onAction {
            handleAction(it)
        }
        if (isValidPhoneNumber(argsUserName)) {
            viewModelScope.launchWithErrorHandling { generateOtp() }
        }
    }

    private suspend fun updateArgsUsername() {
        modifyState { copy(argsUserName = this@VerifyOtpViewModel.argsUserName) }
    }

    private suspend fun getCurrentUser() {
        val currentUser = sessionRepository.getSession().user
        modifyState { copy(currentUser = currentUser) }
    }

    private suspend fun generateOtp() {
        authRepository.generateOtp(argsUserName)
    }


    private suspend fun generatePhoneOtp() {
        startTimer()
        authRepository.generateOtp(argsUserName)
    }

    private suspend fun generateEmailOtp() {
        startTimer()
        authRepository.resendEmailVerificationCode()
    }

    private suspend fun verifyOtp() {
        try {
            val otpPayload = getOtpPayload()
            val currentUser = state.currentUser

            when {
                isPhoneSignUp() && currentUser?.isEmpty == true -> registerWithMobile(otpPayload)
                currentUser?.email?.isNotEmpty() == true && currentUser.isEmpty.not() -> verifyEmail(otpPayload)
                else -> verifyMobilePhone(otpPayload)
            }

            navigateToNextScreen()
        } catch (e: Exception) {
            handleOtpError()
            throw e
        }
    }

    private fun getOtpPayload(): String = state.otpValues.joinToString(separator = "")

    private suspend fun registerWithMobile(otp: String) {
        authRepository.registerWithMobile(
            phone = state.username.ifEmpty { argsUserName },
            otp = otp
        )
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launchWithTimber {
            flow {
                for (ticker in COUNTDOWN_MAX_TIMER downTo 1L) {
                    emit(ticker)
                    delay(DEFAULT_DELAY_IN_MILLISECONDS)
                }
            }.onCompletion {
                modifyState {
                    copy(
                        isCountdownActive = false,
                        timerText = resourceManager.getString(R.string.resend)
                    )
                }
            }.collect {
                modifyState {
                    copy(
                        isCountdownActive = true,
                        timerText = it
                            .formatToMinutesAndSeconds()
                            ?.let { time -> resourceManager.getString(R.string.resend_code_in_time_format, time) }
                            .orEmpty()
                    )
                }
            }
        }
    }

    private suspend fun verifyEmail(otp: String) {
        authRepository.verifyAccountEmail(otp)
    }

    private suspend fun verifyMobilePhone(otp: String) {
        authRepository.verifyAccountMobilePhone(otp)
    }

    private suspend fun navigateToNextScreen() {
        modifyState { copy(shouldNavigateToNextScreen = Consumable(username)) }
    }

    private suspend fun handleOtpError() {
        modifyState { copy(isCodeInvalid = true) }
    }

    override val initialState: VerifyOtpViewState
        get() = VerifyOtpViewState(timerText = resourceManager.getString(R.string.resend))


    private suspend fun handleAction(action: Action) {
        when (action) {
            Action.OnOtpVerify -> {
                verifyOtp()
            }

            is Action.OnResendOtp -> {
                if (state.isCountdownActive) return
                if (state.isEmailLogin || state.currentUser?.email?.isNotEmpty() == true) {
                    generateEmailOtp()
                } else {
                    generatePhoneOtp()
                }
            }

            is Action.OnOtpInputChange -> onOtpInputChange(action.index, action.value)
            Action.OnClickBackButton -> {
                logout()
            }
        }
    }

    private suspend fun logout() {
        if (sessionRepository.getSession().user.isEmpty.not()) {
            authRepository.logout()
            modifyState { copy(shouldNavigateUp = ConsumableUnit()) }
            return
        }
        modifyState { copy(shouldNavigateUp = ConsumableUnit()) }
    }

    private suspend fun onOtpInputChange(index: Int, value: String) {
        modifyState {
            copy(
                otpValues = otpValues.toMutableList().also { values ->
                    values[index] = value
                },
                isCodeInvalid = false
            )
        }
    }

    private fun isPhoneSignUp(): Boolean = isValidPhoneNumber(argsUserName)

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = error.asConsumable()) }
    }

    data class VerifyOtpViewState(
        val isLoading: Boolean = false,
        val isCodeInvalid: Boolean = false,
        val otpValues: List<String> = listOf(*Array(DEFAULT_BP_OTP_LENGTH) { "" }),
        val shouldNavigateToNextScreen: Consumable<String>? = null,
        val shouldNavigateUp: Consumable<Unit>? = null,
        val currentUser: User? = null,
        val error: Consumable<Throwable>? = null,
        val argsUserName: String = "",
        val timerText: String,
        val isCountdownActive: Boolean = false,
    ) {

        val username get() = currentUser?.phoneNumber?.ifEmpty { argsUserName }.orEmpty()

        val isEmailLogin get() = currentUser?.email?.isNotEmpty() ?: false
    }

    sealed interface Action {
        data class OnOtpInputChange(val index: Int, val value: String) : Action
        data object OnOtpVerify : Action
        data object OnResendOtp : Action
        data object OnClickBackButton : Action
    }
}
