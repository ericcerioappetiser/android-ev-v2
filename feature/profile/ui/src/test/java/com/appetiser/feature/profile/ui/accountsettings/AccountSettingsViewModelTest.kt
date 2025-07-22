package com.appetiser.feature.profile.ui.accountsettings

import com.appetiser.core.test.local.base.BaseViewModelTest
import com.appetiser.core.test.local.model.Stub
import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.onboarding.data.UserRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountSettingsViewModelTest : BaseViewModelTest<AccountSettingsViewModel>() {

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var userRepository: UserRepository

    override fun viewModel() = AccountSettingsViewModel(
        userRepository = userRepository,
        authRepository = authRepository
    )

    @Test
    fun onInitial_ShouldEmitUserDetails() = runTest {
        val user = Stub.user

        every {
            userRepository.getCurrentUserFlow()
        } returns flowOf(user)

        viewModel.onInitial()

        val expectedState = viewModel.stateFlow.first()

        advanceUntilIdle()
        assertThat(
            AccountSettingsViewModel.ViewState(
                username = user.email,
                usernameType = user.primaryUsername
            )
        ).isEqualTo(expectedState)
    }
}
