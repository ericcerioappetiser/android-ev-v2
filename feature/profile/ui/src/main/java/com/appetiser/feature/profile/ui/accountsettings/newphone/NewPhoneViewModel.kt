package com.appetiser.feature.profile.ui.accountsettings.newphone

import com.appetiser.baseplate.common.util.PhoneUtils
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_INVALID_CURRENT_CODE = 401
private const val ERROR_DUPLICATE_PHONE = 422
private const val ERROR_INVALID_NEW_CODE = 400

@HiltViewModel
class NewPhoneViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel<NewPhoneViewModel.ViewState, NewPhoneViewModel.Action>() {

    private var token = ""

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
            copy(currentPhone = user.phoneNumber)
        }
        sendCode(user.phoneNumber)
    }

    private suspend fun sendCode(phoneNumber: String) {
        authRepository.generateOtp(phoneNumber)
    }

    private suspend fun handleAction(action: Action) {
        when (action) {
            is Action.ShowPreviousPage -> showPreviousPage()
            is Action.ChangeCurrentCode -> changeCurrentCode(action.index, action.char)
            is Action.SubmitCurrentCode -> submitCurrentCode()
            is Action.ResendCode -> sendCode(
                if (action.isCurrent) {
                    state.currentPhone
                } else {
                    state.newPhone
                }
            )
            is Action.ChangeNewPhone -> changePhone(action.phone)
            is Action.SubmitNewPhone -> submitPhone()
            is Action.ChangeNewCode -> changeNewCode(action.index, action.char)
            is Action.SubmitNewCode -> submitNewCode()
        }
    }

    private suspend fun showPreviousPage() {
        modifyState {
            copy(
                page = if (state.page == NewPhonePage.VERIFY_NEW_PHONE) {
                    NewPhonePage.NEW_PHONE
                } else {
                    NewPhonePage.VERIFY_CURRENT_PHONE
                }
            )
        }
    }

    private suspend fun changeCurrentCode(index: Int, char: String) {
        modifyState {
            copy(
                currentCode = state.currentCode.toMutableList().also {
                    it[index] = char
                },
                hasCurrentCodeError = false
            )
        }
    }

    private suspend fun submitCurrentCode() {
        try {
            token = authRepository.verifyOtp(state.currentCode.joinToString(""))
            modifyState {
                copy(
                    page = NewPhonePage.NEW_PHONE,
                    currentCode = listOf("", "", "", "", "")
                )
            }
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_INVALID_CURRENT_CODE) {
                modifyState {
                    copy(hasCurrentCodeError = true)
                }
            }
        }
    }

    private suspend fun changePhone(phone: String) {
        modifyState {
            copy(
                newPhone = phone,
                hasNewPhoneError = false
            )
        }
    }

    private suspend fun submitPhone() {
        try {
            authRepository.requestPhoneNumber(
                verificationToken = token,
                phoneNumber = state.newPhone
            )
            sendCode(state.newPhone)
            modifyState {
                copy(page = NewPhonePage.VERIFY_NEW_PHONE)
            }
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_DUPLICATE_PHONE) {
                modifyState {
                    copy(hasNewPhoneError = true)
                }
            }
        }
    }

    private suspend fun changeNewCode(index: Int, char: String) {
        modifyState {
            copy(
                newCode = state.newCode.toMutableList().also {
                    it[index] = char
                },
                hasNewCodeError = false
            )
        }
    }

    private suspend fun submitNewCode() {
        try {
            authRepository.verifyChangePhone(
                verificationToken = token,
                code = state.newCode.joinToString("")
            )
            modifyState {
                copy(phoneUpdated = Consumable(true))
            }
        } catch (exception: HttpException) {
            if (exception.code() == ERROR_INVALID_NEW_CODE) {
                modifyState {
                    copy(hasNewCodeError = true)
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
        val page: NewPhonePage = NewPhonePage.VERIFY_CURRENT_PHONE,
        val currentPhone: String = "",
        val currentCode: List<String> = listOf("", "", "", "", ""),
        val hasCurrentCodeError: Boolean = false,
        val newPhone: String = "",
        val hasNewPhoneError: Boolean = false,
        val newCode: List<String> = listOf("", "", "", "", ""),
        val hasNewCodeError: Boolean = false,
        val phoneUpdated: Consumable<Boolean>? = null,
        val error: ConsumableError? = null
    ) {

        val newPhoneValid = PhoneUtils.isValidPhoneNumber(newPhone) && hasNewPhoneError.not()
    }

    sealed interface Action {

        data object ShowPreviousPage : Action

        data class ChangeCurrentCode(
            val index: Int,
            val char: String
        ) : Action

        data object SubmitCurrentCode : Action

        data class ResendCode(
            val isCurrent: Boolean
        ) : Action

        data class ChangeNewPhone(
            val phone: String
        ) : Action

        data object SubmitNewPhone : Action

        data class ChangeNewCode(
            val index: Int,
            val char: String
        ) : Action

        data object SubmitNewCode : Action
    }
}
