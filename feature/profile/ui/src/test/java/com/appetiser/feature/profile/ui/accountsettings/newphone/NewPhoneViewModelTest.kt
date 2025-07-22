package com.appetiser.feature.profile.ui.accountsettings.newphone

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
class NewPhoneViewModelTest : BaseViewModelTest<NewPhoneViewModel>() {

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var userRepository: UserRepository

    override fun viewModel() = NewPhoneViewModel(
        authRepository = authRepository,
        userRepository = userRepository
    )

    override fun onInitial() {
        val user = Stub.user

        coEvery {
            userRepository.getCurrentUser()
        } returns user
        coJustRun {
            authRepository.generateOtp(user.phoneNumber)
        }
    }

    @Test
    fun onInitial_ShouldEmitCurrentPhone() = runTest {
        viewModel.onInitial()

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                currentPhone = Stub.user.phoneNumber
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun showPreviousPage_ShouldEmitCurrentPage_WhenCurrentPageIsVerifyCurrentPhone() = runTest {
        viewModel.action(NewPhoneViewModel.Action.ShowPreviousPage)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                page = NewPhonePage.VERIFY_CURRENT_PHONE
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun showPreviousPage_ShouldEmitVerifyCurrentPhonePage_WhenCurrentPageIsNewPhone() = runTest {
        coEvery {
            authRepository.verifyOtp("")
        } returns ""

        viewModel.action(NewPhoneViewModel.Action.SubmitCurrentCode)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                page = NewPhonePage.NEW_PHONE,
                currentCode = listOf("", "", "", "", "")
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun showPreviousPage_ShouldEmitNewPhonePage_WhenCurrentPageIsVerifyNewPhone() = runTest {
        coJustRun {
            authRepository.requestPhoneNumber(
                verificationToken = "",
                phoneNumber = ""
            )
        }
        coJustRun {
            authRepository.generateOtp("")
        }

        viewModel.action(NewPhoneViewModel.Action.SubmitNewPhone)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                page = NewPhonePage.VERIFY_NEW_PHONE
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeCurrentCode_ShouldEmitCurrentCode() = runTest {
        viewModel.action(
            NewPhoneViewModel.Action.ChangeCurrentCode(
                index = 0,
                char = "0"
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                currentCode = listOf("0", "", "", "", "")
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeCurrentCode_ShouldEmitNoCurrentCodeError_WhenTypingCurrentCode() = runTest {
        viewModel.action(
            NewPhoneViewModel.Action.ChangeCurrentCode(
                index = 0,
                char = "0"
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                currentCode = listOf("0", "", "", "", ""),
                hasCurrentCodeError = false
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitCurrentCode_ShouldEmitNewPhonePage_WhenCurrentCodeIsSubmitted() = runTest {
        coEvery {
            authRepository.verifyOtp("")
        } returns ""

        viewModel.action(NewPhoneViewModel.Action.SubmitCurrentCode)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                page = NewPhonePage.NEW_PHONE,
                currentCode = listOf("", "", "", "", "")
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitCurrentCode_ShouldEmitCurrentCodeError_WhenCurrentCodeIsInvalid() = runTest {
        coEvery {
            authRepository.verifyOtp("")
        } throws Stub.incorrectPasswordError

        viewModel.action(NewPhoneViewModel.Action.SubmitCurrentCode)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                hasCurrentCodeError = true
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changePhone_ShouldEmitPhone() = runTest {
        val phone = Stub.user.phoneNumber

        viewModel.action(
            NewPhoneViewModel.Action.ChangeNewPhone(
                phone = phone
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                newPhone = phone
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changePhone_ShouldEmitNoPhoneError_WhenTypingPhone() = runTest {
        val phone = Stub.user.phoneNumber

        viewModel.action(
            NewPhoneViewModel.Action.ChangeNewPhone(
                phone = phone
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                newPhone = phone,
                hasNewPhoneError = false
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitPhone_ShouldEmitVerifyNewPhonePage_WhenChangePhoneIsRequested() = runTest {
        coJustRun {
            authRepository.requestPhoneNumber(
                verificationToken = "",
                phoneNumber = ""
            )
        }
        coJustRun {
            authRepository.generateOtp("")
        }

        viewModel.action(NewPhoneViewModel.Action.SubmitNewPhone)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                page = NewPhonePage.VERIFY_NEW_PHONE
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitPhone_ShouldEmitPhoneError_WhenPhoneIsDuplicate() = runTest {
        coEvery {
            authRepository.requestPhoneNumber(
                verificationToken = "",
                phoneNumber = ""
            )
        } throws Stub.duplicateEmailError
        coJustRun {
            authRepository.generateOtp("")
        }

        viewModel.action(NewPhoneViewModel.Action.SubmitNewPhone)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                hasNewPhoneError = true
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeNewCode_ShouldEmitNewCode() = runTest {
        viewModel.action(
            NewPhoneViewModel.Action.ChangeNewCode(
                index = 0,
                char = "0"
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                newCode = listOf("0", "", "", "", "")
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun changeNewCode_ShouldEmitNoNewCodeError_WhenTypingNewCode() = runTest {
        viewModel.action(
            NewPhoneViewModel.Action.ChangeNewCode(
                index = 0,
                char = "0"
            )
        )

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                newCode = listOf("0", "", "", "", ""),
                hasNewCodeError = false
            )
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitNewCode_ShouldEmitConsumableCondition_WhenNewCodeIsSubmitted() = runTest {
        coJustRun {
            authRepository.verifyChangePhone(
                verificationToken = "",
                code = ""
            )
        }

        viewModel.action(NewPhoneViewModel.Action.SubmitNewCode)

        val expectedState = viewModel.stateFlow.first().toString()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                phoneUpdated = Consumable(true)
            ).toString()
        ).isEqualTo(expectedState)
    }

    @Test
    fun submitNewCode_ShouldEmitNewCodeError_WhenNewCodeIsInvalid() = runTest {
        coEvery {
            authRepository.verifyChangePhone(
                verificationToken = "",
                code = ""
            )
        } throws Stub.invalidCodeError

        viewModel.action(NewPhoneViewModel.Action.SubmitNewCode)

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            viewModel.state.copy(
                hasNewCodeError = true
            )
        ).isEqualTo(expectedState)
    }
}
