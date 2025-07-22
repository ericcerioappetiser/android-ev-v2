package com.appetiser.feature.auth.ui.getstarted

import androidx.lifecycle.viewModelScope
import com.appetiser.baseplate.common.util.PhoneUtils
import com.appetiser.baseplate.common.util.PhoneUtils.isValidEmail
import com.appetiser.baseplate.common.util.PhoneUtils.isValidPhoneNumber
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.common.android.util.ResourceManager
import com.appetiser.core.domain.usernamecheck.UsernameNotFoundException
import com.appetiser.core.domain.usernamecheck.UsernameType
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.auth.ui.getstarted.GetStartedScreenViewModel.ActionState
import com.appetiser.feature.auth.ui.getstarted.GetStartedScreenViewModel.GetStartedViewState
import com.appetiser.feature.auth.ui.utils.PhoneNumberHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GetStartedScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val phoneNumberHelper: PhoneNumberHelper,
    private val resourceManager: ResourceManager
) : BaseViewModel<GetStartedViewState, ActionState>() {


    init {
        onAction { handleAction(it) }
        getCountryCode()
    }

    override val initialState: GetStartedViewState
        get() = GetStartedViewState()

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = ConsumableError(error)) }
    }

    private fun getCountryCode() {
        viewModelScope.launch {
            val countryCode = phoneNumberHelper.getDeviceCountryDialingCode(
                countryCode = resourceManager.getDeviceCountryNameCode()
            )
            modifyState { copy(countryCode = countryCode.orEmpty()) }
        }
    }




    private suspend fun handleAction(action: ActionState) {
        when (action) {
            ActionState.OnClickContinueViaApple -> {
                // TODO
            }

            ActionState.OnClickContinueViaFacebook -> {
                // TODO
            }

            ActionState.OnClickContinueViaGoogle -> {
                // TODO
            }

            is ActionState.OnClickContinue -> {
                login()
            }

            is ActionState.OnUserNameChange -> {
                val sanitizeUsername = sanitizePhoneNumberOrEmail(action.userName)
                modifyState { copy(userName = sanitizeUsername) }
            }
        }
    }

    private suspend fun login() {
        val sanitizeUsername = sanitizePhoneNumberOrEmail(state.userName)
        modifyState { copy(isLoading = true) }

        val result = authRepository.checkUsername(sanitizeUsername)
        modifyState { copy(isLoading = false) }
        result.fold(
            onSuccess = { user ->
                val isPhoneNumber = PhoneNumberHelper().isNumberDigitsOnly(user.userName)

                val destination = if (isPhoneNumber) {
                    NavigateDestination.LOGIN_PHONE
                } else {
                    NavigateDestination.LOGIN
                }
                modifyState {
                    copy(onNavigateTo = destination.asConsumable())
                }
            },
            onFailure = { error ->
                when (error) {
                    is UsernameNotFoundException -> {
                        val destination = when (requireNotNull(error.usernameType)) {
                            UsernameType.Email -> NavigateDestination.CREATE_PASSWORD
                            UsernameType.Phone -> NavigateDestination.VERIFY_PHONE
                        }
                        modifyState {
                            copy(onNavigateTo = destination.asConsumable())
                        }
                    }

                    else -> {
                        throw error
                    }
                }
            }
        )
    }

    private fun sanitizePhoneNumberOrEmail(input: String): String = if (phoneNumberHelper.isNumberDigitsOnly(input)) {
        input.replace(" ", "")
    } else {
        input
    }

    data class GetStartedViewState(
        val isLoading: Boolean = false,
        val userName: String = "",
        val onNavigateTo: Consumable<NavigateDestination>? = null,
        val error: ConsumableError? = null,
        val countryCode: String = ""
    ) {

        val isValidInput by lazy {
            isValidEmail(userName) || isValidPhoneNumber(userName)
        }
        val isPhoneNumber by lazy {
            isValidPhoneNumber(userName)
        }

        val formattedUsername = if (isValidPhoneNumber(userName)) {
            PhoneUtils.formatPhoneNumberWithCountryCode(
                phoneNumber = userName, prefix = countryCode
            )
        } else {
            userName
        }
    }

    enum class NavigateDestination {
        LOGIN,
        LOGIN_PHONE,
        CREATE_PASSWORD,
        VERIFY_PHONE
    }

    sealed interface ActionState {
        data class OnUserNameChange(val userName: String) : ActionState
        data object OnClickContinue : ActionState
        data object OnClickContinueViaGoogle : ActionState
        data object OnClickContinueViaFacebook : ActionState
        data object OnClickContinueViaApple : ActionState
    }
}
