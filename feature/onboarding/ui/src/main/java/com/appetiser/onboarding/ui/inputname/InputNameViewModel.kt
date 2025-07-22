package com.appetiser.onboarding.ui.inputname

import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableUnit
import com.appetiser.core.domain.user.UpdateUserRequest
import com.appetiser.core.domain.user.User
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InputNameViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<InputNameViewModel.InputViewState, InputNameViewModel.InputAction>() {


    init {
        onAction {
            handleAction(it)
        }
    }

    override val initialState: InputViewState
        get() = InputViewState()

    override suspend fun onInitial() {
        super.onInitial()
        getUser()
    }

    private suspend fun handleAction(action: InputAction) {
        when (action) {
            InputAction.Submit -> {
                submitUserDetails()
            }

            is InputAction.UpdateFirstName -> {
                modifyState { copy(firstNameInput = action.firstName) }
            }

            is InputAction.UpdateLastName -> {
                modifyState { copy(lastNameInput = action.lastName) }
            }

            InputAction.OnClickBackButton -> {
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

    private suspend fun submitUserDetails() {
        val currentState = state
        val firstName = currentState.firstNameInput
        val lastName = currentState.lastNameInput

        try {
            modifyState { copy(isLoading = true) }
            currentState.user?.let { currentUser ->
                userRepository.updateCurrentUser(
                    user = UpdateUserRequest(lastName = lastName, firstName = firstName, id = currentUser.id)
                )
                modifyState { copy(shouldNavigateToYourBirthdayScreen = ConsumableUnit()) }
            } ?: run {
                // TODO add firebase analytics here to notify us
            }
        } finally {
            modifyState {
                copy(
                    isLoading = false,
                    shouldNavigateToYourBirthdayScreen = Unit.asConsumable()
                )
            }
        }
    }

    data class InputViewState(
        val isLoading: Boolean = false,
        val user: User? = null,
        val lastNameInput: String = "",
        val firstNameInput: String = "",
        val shouldNavigateToYourBirthdayScreen: Consumable<Unit>? = null,
        val shouldExitAppOnBack: Consumable<Unit>? = null,
        val error: Consumable<Throwable>? = null,
        val shouldNavigateUp: Consumable<Unit>? = null
    ) {

        val isSubmitEnabled: Boolean
            get() = lastNameInput.isNotEmpty() && firstNameInput.isNotEmpty()
    }

    sealed interface InputAction {
        data object Submit : InputAction
        data class UpdateFirstName(val firstName: String) : InputAction
        data class UpdateLastName(val lastName: String) : InputAction
        data object OnClickBackButton : InputAction
    }

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = error.asConsumable()) }
    }
}
