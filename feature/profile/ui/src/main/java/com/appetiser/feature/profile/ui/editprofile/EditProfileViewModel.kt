package com.appetiser.feature.profile.ui.editprofile

import com.appetiser.baseplate.common.ext.toLocalDate
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.domain.user.UpdateUserRequest
import com.appetiser.core.domain.user.User
import com.appetiser.feature.onboarding.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.LocalDate
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<EditProfileViewModel.ViewState, EditProfileViewModel.ActionState>() {

    override val initialState: ViewState
        get() = ViewState()

    init {
        onAction {
            handleAction(it)
        }
    }

    override suspend fun onInitial() {
        getUser()
    }

    private suspend fun handleAction(actionState: ActionState) {
        when (actionState) {
            is ActionState.SelectProfilePhoto -> uploadPhoto(actionState.photoFile)
            is ActionState.ChangeFirstName -> changeFirstName(actionState.firstName)
            is ActionState.ChangeLastName -> changeLastName(actionState.lastName)
            is ActionState.SelectBirthdate -> selectBirthdate(actionState.birthdateMillis)
            is ActionState.SaveEditedProfile -> editProfile()
        }
    }

    private suspend fun getUser() {
        val user = userRepository.getCurrentUser()
        modifyState {
            copy(
                user = user,
                firstName = user.firstName,
                lastName = user.lastName,
                birthdate = user.birthdate,
                photoUrl = user.avatar?.url.orEmpty()
            )
        }
    }

    private suspend fun uploadPhoto(photoFile: File?) {
        photoFile?.let {
            val media = userRepository.uploadUserAvatar(photoFile)
            modifyState {
                copy(photoUrl = media.url)
            }
        }
    }

    private suspend fun changeFirstName(firstName: String) {
        modifyState {
            copy(firstName = firstName)
        }
    }

    private suspend fun changeLastName(lastName: String) {
        modifyState {
            copy(lastName = lastName)
        }
    }

    private suspend fun selectBirthdate(birthdateMillis: Long?) {
        modifyState {
            copy(birthdate = birthdateMillis?.toLocalDate())
        }
    }

    private suspend fun editProfile() {
        state.user?.let {
            val updatedUser = userRepository.updateCurrentUser(
                user = UpdateUserRequest(
                    birthdate = state.birthdate,
                    firstName = state.firstName,
                    lastName = state.lastName,
                    id = it.id
                )
            )
            modifyState {
                copy(
                    user = updatedUser,
                    profileSaved = Consumable(true)
                )
            }
        } ?: run {
            // TODO add firebase analytics here to notify us
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
        val user: User? = null,
        val firstName: String = "",
        val lastName: String = "",
        val birthdate: LocalDate? = null,
        val photoUrl: String = "",
        val profileSaved: Consumable<Boolean>? = null,
        val error: ConsumableError? = null
    ) {

        val enableSave = user?.let {
            (firstName != it.firstName && firstName.isNotBlank()) ||
                (lastName != it.lastName && lastName.isNotBlank()) ||
                birthdate != it.birthdate
        } ?: false
    }

    sealed interface ActionState {

        data class SelectProfilePhoto(
            val photoFile: File?
        ) : ActionState

        data class ChangeFirstName(
            val firstName: String
        ) : ActionState

        data class ChangeLastName(
            val lastName: String
        ) : ActionState

        data class SelectBirthdate(
            val birthdateMillis: Long?
        ) : ActionState

        data object SaveEditedProfile : ActionState
    }
}
