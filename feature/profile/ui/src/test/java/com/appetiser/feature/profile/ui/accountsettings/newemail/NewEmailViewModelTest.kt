package com.appetiser.feature.profile.ui.accountsettings.newemail

import com.appetiser.core.common.android.base.BaseViewModel.Consumable
import com.appetiser.core.test.local.base.BaseViewModelTest
import com.appetiser.core.test.local.model.Stub
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.onboarding.data.UserRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class NewEmailViewModelTest : BaseViewModelTest<NewEmailViewModel>() {

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var userRepository: UserRepository

    override fun viewModel() = NewEmailViewModel(
        userRepository = userRepository,
        authRepository = authRepository
    )

    @Test
    fun showPreviousPage_ShouldEmitCurrentPage_WhenCurrentPageIsYourPassword() = runTest {
        viewModel.action(NewEmailViewModel.Action.ShowPreviousPage)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                page = NewEmailPage.YOUR_PASSWORD
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun showPreviousPage_ShouldEmitYourPasswordPage_WhenCurrentPageIsNewEmail() = runTest {
        coEvery {
            authRepository.verifyPassword("")
        } returns ""

        viewModel.action(NewEmailViewModel.Action.SubmitPassword)
        viewModel.action(NewEmailViewModel.Action.ShowPreviousPage)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                page = NewEmailPage.YOUR_PASSWORD
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun showPreviousPage_ShouldEmitNewEmailPage_WhenCurrentPageIsVerifyEmail() = runTest {
        coJustRun {
            authRepository.requestChangeEmail(
                verificationToken = "",
                newEmail = ""
            )
        }

        viewModel.action(NewEmailViewModel.Action.SubmitEmail)
        viewModel.action(NewEmailViewModel.Action.ShowPreviousPage)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                page = NewEmailPage.NEW_EMAIL
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changePassword_ShouldEmitPassword() = runTest {
        val password = "Test@123"

        viewModel.action(
            NewEmailViewModel.Action.ChangePassword(
                password = password
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                password = password
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changePassword_ShouldEmitNoPasswordError_WhenTypingPassword() = runTest {
        val password = "Test@123"

        viewModel.action(
            NewEmailViewModel.Action.ChangePassword(
                password = password
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                password = password,
                hasPasswordError = false
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun forgotPassword_ShouldEmitConsumableEmail() = runTest {
        val user = Stub.user

        coEvery {
            userRepository.getCurrentUser()
        } returns user

        viewModel.action(NewEmailViewModel.Action.ForgotPassword)

        val expectedState = viewModel.stateFlow.first().toString()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                passwordForgot = Consumable(user.email)
            ).toString()
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitPassword_ShouldEmitNewEmailPage_WhenPasswordIsSubmitted() = runTest {
        val password = ""
        val resultState = NewEmailViewModel.ViewState(page = NewEmailPage.NEW_EMAIL)

        coEvery { authRepository.verifyPassword(password) } returns password

        viewModel.action(NewEmailViewModel.Action.SubmitPassword)

        advanceUntilIdle()
        assertThat(viewModel.stateFlow.first()).isEqualTo(resultState)
    }

    @Test
    fun submitPassword_ShouldEmitPasswordError_WhenPasswordIsIncorrect() = runTest {
        coEvery {
            authRepository.verifyPassword("")
        } throws Stub.incorrectPasswordError

        viewModel.action(NewEmailViewModel.Action.SubmitPassword)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                hasPasswordError = true
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeEmail_ShouldEmitEmail() = runTest {
        val email = Stub.user.email

        viewModel.action(
            NewEmailViewModel.Action.ChangeEmail(
                email = email
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                email = email
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeEmail_ShouldEmitNoEmailError_WhenTypingEmail() = runTest {
        val email = Stub.user.email

        viewModel.action(
            NewEmailViewModel.Action.ChangeEmail(
                email = email
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                email = email,
                hasEmailError = false
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitEmail_ShouldEmitVerifyEmailPage_WhenChangeEmailIsRequested() = runTest {
        coJustRun {
            authRepository.requestChangeEmail(
                verificationToken = "",
                newEmail = ""
            )
        }

        viewModel.action(NewEmailViewModel.Action.SubmitEmail)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                page = NewEmailPage.VERIFY_EMAIL
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitEmail_ShouldEmitEmailError_WhenEmailIsDuplicate() = runTest {
        coEvery {
            authRepository.requestChangeEmail(
                verificationToken = "",
                newEmail = ""
            )
        } throws Stub.duplicateEmailError

        viewModel.action(NewEmailViewModel.Action.SubmitEmail)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                hasEmailError = true
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeCode_ShouldEmitCode() = runTest {
        viewModel.action(
            NewEmailViewModel.Action.ChangeCode(
                index = 0,
                char = "0"
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                code = listOf("0", "", "", "", "")
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitCode_ShouldEmitConsumableCondition_WhenCodeIsSubmitted() = runTest {
        coJustRun {
            authRepository.verifyChangeEmail(
                verificationToken = "",
                code = ""
            )
        }

        viewModel.action(NewEmailViewModel.Action.SubmitCode)

        val expectedState = viewModel.stateFlow.first().toString()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                emailUpdated = Consumable(true)
            ).toString()
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitCode_ShouldEmitCodeError_WhenCodeIsInvalid() = runTest {
        coEvery {
            authRepository.verifyChangeEmail(
                verificationToken = "",
                code = ""
            )
        } throws Stub.invalidCodeError

        viewModel.action(NewEmailViewModel.Action.SubmitCode)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            NewEmailViewModel.ViewState(
                hasCodeError = true
            )
        ).isEqualTo(expectedState)
    }
}
