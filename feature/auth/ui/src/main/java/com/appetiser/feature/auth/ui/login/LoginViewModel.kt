package com.appetiser.feature.auth.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.common.android.base.ConsumableUnit
import com.appetiser.core.domain.user.User
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.auth.ui.LoginViaEmailRoute
import com.appetiser.feature.auth.ui.login.LoginViewModel.Action
import com.appetiser.feature.auth.ui.login.LoginViewModel.LoginViewState
import com.appetiser.feature.auth.ui.utils.AuthState
import com.appetiser.feature.auth.ui.utils.AuthenticationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
) : BaseViewModel<LoginViewState, Action>() {

    private val email = savedStateHandle.toRoute<LoginViaEmailRoute>().email

    init {
        onAction { handleAction(it) }
    }

    override val initialState: LoginViewState
        get() = LoginViewState(
            email = email
        )

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = error.asConsumable()) }
    }

    private suspend fun handleAction(action: Action) {
        when (action) {
            Action.OnLogin -> {
                login()
            }

            is Action.OnPasswordChange -> modifyState { copy(password = action.password) }
        }
    }

    private suspend fun login() {
        try {
            val password = state.password
            modifyState { copy(isLoading = true) }
            val session = authRepository.loginWithEmail(
                username = email,
                password = password
            )
            modifyState {
                copy(
                    authState = AuthenticationUtils.getUserAuthState(
                        user = session.user,
                    ).asConsumable(),
                    user = session.user
                )
            }
        } finally {
            modifyState { copy(isLoading = false) }
        }
    }


    data class LoginViewState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val shouldNavigateToMainScreen: ConsumableUnit? = null,
        val shouldNavigateToBirthdate: ConsumableUnit? = null,
        val shouldNavigateToAddEmail: ConsumableUnit? = null,
        val shouldNavigationNoProfile: ConsumableUnit? = null,
        val shouldNavigateToFullName: ConsumableUnit? = null,
        val shouldNavigateToVerify: ConsumableUnit? = null,
        val authState: Consumable<AuthState>? = null,
        val error: ConsumableError? = null,
        val user: User? = null
    )


    sealed interface Action {
        data class OnPasswordChange(val password: String) : Action
        data object OnLogin : Action
    }
}
