package com.appetiser.feature.profile.ui

import com.appetiser.core.test.local.base.BaseViewModelTest
import com.appetiser.core.test.local.model.Stub
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
class ProfileViewModelTest : BaseViewModelTest<ProfileViewModel>() {

    @MockK
    lateinit var userRepository: UserRepository

    override fun viewModel() = ProfileViewModel(
        userRepository = userRepository
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
            ProfileViewModel.ViewState(
                fullName = user.fullName,
                username = user.email,
                photoUrl = user.avatar?.url.orEmpty()
            )
        ).isEqualTo(expectedState)
    }
}
