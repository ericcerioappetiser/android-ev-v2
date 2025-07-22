package com.appetiser.feature.profile.ui.editprofile

import com.appetiser.core.test.local.base.BaseViewModelTest
import com.appetiser.core.test.local.model.Stub
import com.appetiser.feature.onboarding.data.UserRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class EditProfileViewModelTest : BaseViewModelTest<EditProfileViewModel>() {

    @MockK
    lateinit var userRepository: UserRepository

    override fun viewModel() = EditProfileViewModel(
        userRepository = userRepository
    )

    override fun onInitial() {
        coEvery {
            userRepository.getCurrentUser()
        } returns Stub.user
    }

    @Test
    fun onInitial_ShouldEmitUserDetails() = runTest {
        val user = Stub.user

        viewModel.onInitial()

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                user = user,
                firstName = user.firstName,
                lastName = user.lastName,
                birthdate = user.birthdate,
                photoUrl = user.avatar?.url.orEmpty()
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun uploadPhoto_ShouldEmitPhotoUrl() = runTest {
        val photoFile = File("")
        val media = Stub.media

        coEvery {
            userRepository.uploadUserAvatar(photoFile)
        } returns media

        viewModel.action(
            EditProfileViewModel.ActionState.SelectProfilePhoto(
                photoFile = photoFile
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                photoUrl = media.url
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeFirstName_ShouldEmitFirstName() = runTest {
        val firstName = Stub.user.firstName

        viewModel.action(
            EditProfileViewModel.ActionState.ChangeFirstName(
                firstName = firstName
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                firstName = firstName
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeLastName_ShouldEmitLastName() = runTest {
        val lastName = Stub.user.lastName

        viewModel.action(
            EditProfileViewModel.ActionState.ChangeLastName(
                lastName = lastName
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                lastName = lastName
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun selectBirthdate_ShouldEmitBirthdate() = runTest {
        val birthdate = Stub.user.birthdate

        viewModel.action(
            EditProfileViewModel.ActionState.SelectBirthdate(
                birthdateMillis = birthdate
                    ?.atStartOfDayIn(TimeZone.currentSystemDefault())
                    ?.toEpochMilliseconds()
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                birthdate = birthdate
            )
        ).isEqualTo(expectedState)
    }
}
