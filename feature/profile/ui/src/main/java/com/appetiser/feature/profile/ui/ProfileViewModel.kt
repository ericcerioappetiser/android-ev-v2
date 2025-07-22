package com.appetiser.feature.profile.ui

import androidx.lifecycle.viewModelScope
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.domain.user.UsernameType
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<ProfileViewModel.ViewState, ProfileViewModel.ActionState>() {

    override val initialState: ViewState
        get() = ViewState()

    override suspend fun onInitial() {
        viewModelScope.launch {
            userRepository
                .getCurrentUserFlow()
                .collect { user ->
                    modifyState {
                        copy(
                            fullName = user.fullName,
                            username = if (user.primaryUsername == UsernameType.PHONE_NUMBER) {
                                user.phoneNumber
                            } else {
                                user.email
                            },
                            photoUrl = user.avatar?.url.orEmpty()
                        )
                    }
                }
        }
    }

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState {
            copy(error = ConsumableError(error))
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val fullName: String = "",
        val username: String = "",
        val photoUrl: String = "",
        val error: ConsumableError? = null
    )

    sealed interface ActionState
}
