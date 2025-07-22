package com.appetiser.feature.auth.ui.createpassword

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.auth.ui.SignUpViaEmailRoute
import com.appetiser.feature.auth.ui.createpassword.CreatePasswordViewModel.CreatePasswordViewState
import com.appetiser.feature.auth.ui.createpassword.CreatePasswordViewModel.InputAction
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CreatePasswordViewState, InputAction>() {

    private val email = savedStateHandle.toRoute<SignUpViaEmailRoute>().email

    init {
        onAction { handleAction(it) }
    }

    override val initialState: CreatePasswordViewState
        get() = CreatePasswordViewState(
            email = email
        )

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = ConsumableError(error)) }
    }

    data class CreatePasswordViewState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val shouldNavigateToNextScreen: Consumable<String>? = null,
        val error: ConsumableError? = null
    )

    sealed class CreatePasswordActionState {
        data class NavigateToNextScreen(val email: String) : CreatePasswordActionState()
    }

    sealed interface ErrorState {
        data class CommonError(val throwable: Throwable) : ErrorState
    }

    sealed interface InputAction {
        data object ClickContinue : InputAction
        data class InputEmail(val email: String) : InputAction
        data class InputPassword(val password: String) : InputAction
    }

    private suspend fun handleAction(action: InputAction) {
        when (action) {
            InputAction.ClickContinue -> {
                signup()
            }

            is InputAction.InputEmail -> {
                modifyState { copy(email = action.email) }
            }

            is InputAction.InputPassword -> {
                modifyState { copy(password = action.password) }
            }
        }
    }

    private suspend fun signup() {
        val currentState = state
        val email = currentState.email
        val password = currentState.password
        modifyState { copy(isLoading = true) }

        authRepository.registerWithEmail(email, password, password)
        modifyState {
            copy(isLoading = false, shouldNavigateToNextScreen = Consumable(email))
        }
    }
}
