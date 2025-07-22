package com.appetiser.onboarding.ui.profile

import androidx.lifecycle.viewModelScope
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableUnit
import com.appetiser.core.common.android.util.dispatchers.DispatcherProvider
import com.appetiser.core.network.SessionProvider
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class YourProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionProvider: SessionProvider,
    private val dispatchers: DispatcherProvider,
) : BaseViewModel<YourProfileViewModel.YourProfileViewState, YourProfileViewModel.ActionState>() {


    init {
        onAction {
            handleAction(it)
        }
    }


    override val initialState: YourProfileViewState
        get() = YourProfileViewState()

    private suspend fun handleAction(action: ActionState) {
        when (action) {
            ActionState.ClickContinue -> {
                submitProfilePhoto()
            }

            ActionState.ShowPermissionDialog -> {
                modifyState { copy(showPermission = true) }
            }

            is ActionState.OnUpdateMediaPath -> {
                viewModelScope.launch {
                    modifyState { copy(file = action.file) }
                }
            }


            ActionState.OnClickSkip -> {
                onboardingCompleted()
            }
        }
    }

    private suspend fun onboardingCompleted() {
        userRepository.onBoardingCompleted()
        modifyState { copy(onOnboardingCompleted = ConsumableUnit()) }
    }

    private suspend fun submitProfilePhoto() {
        try {
            viewModelScope.launchWithErrorHandling(dispatchers.io) {
                val imageUri = state.file
                imageUri?.let {
                    modifyState { copy(isLoading = true) }
                    userRepository.uploadUserAvatar(it)
                } ?: {
                    throw FileNotFoundException()
                }
                modifyState { copy(onOnboardingCompleted = ConsumableUnit()) }
            }
        } finally {
            modifyState { copy(isLoading = true) }
        }
    }



    data class YourProfileViewState(
        val isLoading: Boolean = false,
        val showPermission: Boolean = false,
        val onOnboardingCompleted: Consumable<Unit>? = null,
        val shouldShowSuccessfulUploadSnackbar: Consumable<Unit>? = null,
        val error: Consumable<Throwable>? = null,
        val file: File? = null,
    )

    sealed interface ActionState {
        data object ClickContinue : ActionState
        data object ShowPermissionDialog : ActionState
        data class OnUpdateMediaPath(
            val file: File?
        ) : ActionState
        data object OnClickSkip : ActionState
    }

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = error.asConsumable()) }
    }
}
