package com.appetiser.onboarding.ui.yourbirthday

import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableUnit
import com.appetiser.core.domain.user.UpdateUserRequest
import com.appetiser.core.domain.user.User
import com.appetiser.feature.onboarding.data.UserRepository
import com.appetiser.onboarding.ui.utils.DateUtils
import com.appetiser.onboarding.ui.utils.DateUtils.DATE_FORMAT_PATTERN
import com.appetiser.onboarding.ui.yourbirthday.YourBirthdayViewModel.YourBirthdayViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class YourBirthdayViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<
    YourBirthdayViewState,
    YourBirthdayViewModel.ActionState
    >() {

    init {
        onAction {
            handleAction(it)
        }
    }

    override val initialState: YourBirthdayViewState
        get() = YourBirthdayViewState()

    override suspend fun onInitial() {
        super.onInitial()
        getUser()
    }

    private suspend fun handleAction(action: ActionState) {
        when (action) {
            is ActionState.GetSelectedDate -> {
                modifyState { copy(birthdayDate = action.selectedDate) }
            }

            ActionState.ClickDateOfBirth -> {
                modifyState { copy(showDialog = true) }
            }

            ActionState.ClickDismissDatePicker -> {
                modifyState { copy(showDialog = false) }
            }

            ActionState.ClickContinue -> {
                submitUserBirthDate()
            }
            ActionState.OnClickBackButton -> {
                modifyState { copy(shouldNavigateUp = ConsumableUnit()) }
            }
        }
    }
    private suspend fun getUser() {
        val user = userRepository.getCurrentUser()
        modifyState {
            copy(
                user = user
            )
        }
    }
    private suspend fun submitUserBirthDate() {
        val selectedBirthdate = state.birthdayDate

        try {
            modifyState { copy(isLoading = true) }
            val userBirthDate = DateUtils.convertStringToLocalDate(selectedBirthdate)
            state.user?.let { currentUser ->
                userRepository.updateCurrentUser(
                    user = UpdateUserRequest(
                        birthdate = userBirthDate,
                        firstName = state.user?.firstName.orEmpty(),
                        lastName = state.user?.lastName.orEmpty(),
                        id = currentUser.id
                    )
                )

                modifyState {
                    copy(shouldNavigateToUploadProfileScreen = ConsumableUnit())
                }
            } ?: run {
                // TODO add firebase analytics here to notify us
            }
        } finally {
            modifyState { copy(isLoading = false) }
        }
    }

    data class YourBirthdayViewState(
        val isLoading: Boolean = false,
        val birthdayDate: String = "",
        val showDialog: Boolean = false,
        val shouldNavigateToUploadProfileScreen: Consumable<Unit>? = null,
        val error: Consumable<Throwable>? = null,
        val shouldExitAppOnBack: Consumable<Unit>? = null,
        val shouldNavigateUp: Consumable<Unit>? = null,
        val user: User? = null
    ) {

        val initialDate = birthdayDate.ifEmpty { DATE_FORMAT_PATTERN.uppercase() }

        val isContinueEnabled: Boolean
            get() = birthdayDate.isNotEmpty()
    }

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = error.asConsumable()) }
    }

    sealed interface ActionState {
        data class GetSelectedDate(val selectedDate: String) : ActionState
        data object ClickDateOfBirth : ActionState
        data object ClickDismissDatePicker : ActionState
        data object ClickContinue : ActionState
        data object OnClickBackButton : ActionState
    }
}
