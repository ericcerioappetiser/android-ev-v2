package com.appetiser.feature.profile.ui.accountsettings.newpassword

import com.appetiser.core.common.android.base.BaseViewModel.Consumable
import com.appetiser.core.test.local.base.BaseViewModelTest
import com.appetiser.core.test.local.model.Stub
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.onboarding.data.UserRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class NewPasswordViewModelTest : BaseViewModelTest<NewPasswordViewModel>() {

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var userRepository: UserRepository

    override fun viewModel() = NewPasswordViewModel(
        userRepository = userRepository,
        authRepository = authRepository
    )

    override fun onInitial() {
        coEvery {
            userRepository.getCurrentUser()
        } returns Stub.user
    }

    @Test
    fun onInitial_ShouldEmitEmail() = runTest {
        viewModel.onInitial()

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                email = Stub.user.email
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun showPreviousPage_ShouldEmitCurrentPage_WhenCurrentPageIsYourPassword() = runTest {
        viewModel.action(NewPasswordViewModel.Action.ShowPreviousPage)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                page = NewPasswordPage.YOUR_PASSWORD
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun showPreviousPage_ShouldEmitYourPasswordPage_WhenCurrentPageIsNewPassword() = runTest {
        coEvery {
            authRepository.verifyPassword("")
        } returns ""

        viewModel.action(NewPasswordViewModel.Action.SubmitCurrentPassword)
        viewModel.action(NewPasswordViewModel.Action.ShowPreviousPage)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                page = NewPasswordPage.YOUR_PASSWORD
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeCurrentPassword_ShouldEmitCurrentPassword() = runTest {
        val password = "Test@123"

        viewModel.action(
            NewPasswordViewModel.Action.ChangeCurrentPassword(
                password = password
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                currentPassword = password
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeCurrentPassword_ShouldEmitNoCurrentPasswordError_WhenTypingCurrentPassword() = runTest {
        val password = "Test@123"

        viewModel.action(
            NewPasswordViewModel.Action.ChangeCurrentPassword(
                password = password
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                currentPassword = password,
                hasCurrentPasswordError = false
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitCurrentPassword_ShouldEmitNewPasswordPage_WhenCurrentPasswordIsSubmitted() = runTest {
        coEvery {
            authRepository.verifyPassword("")
        } returns ""

        viewModel.action(NewPasswordViewModel.Action.SubmitCurrentPassword)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                page = NewPasswordPage.NEW_PASSWORD
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitCurrentPassword_ShouldEmitCurrentPasswordError_WhenCurrentPasswordIsIncorrect() = runTest {
        coEvery {
            authRepository.verifyPassword("")
        } throws Stub.incorrectPasswordError

        viewModel.action(NewPasswordViewModel.Action.SubmitCurrentPassword)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                hasCurrentPasswordError = true
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeNewPassword_ShouldEmitNewPassword() = runTest {
        val password = "Test@123"

        viewModel.action(
            NewPasswordViewModel.Action.ChangeNewPassword(
                password = password
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                newPassword = password
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitNewPassword_ShouldEmitConsumableCondition_WhenNewPasswordIsSubmitted() = runTest {
        coEvery {
            authRepository.changePassword(
                oldPassword = "",
                newPassword = "",
                newPasswordConfirmation = ""
            )
        } returns true

        viewModel.action(NewPasswordViewModel.Action.SubmitNewPassword)

        val expectedState = viewModel.stateFlow.first().toString()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                passwordUpdated = Consumable(true)
            ).toString()
        ).isEqualTo(expectedState)
    }
}
