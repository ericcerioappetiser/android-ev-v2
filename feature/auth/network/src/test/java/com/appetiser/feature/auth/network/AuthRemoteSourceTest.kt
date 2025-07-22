package com.appetiser.feature.auth.network

import com.appetiser.core.network.AuthApiService
import com.appetiser.core.network.model.base.BaseResponse
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class AuthRemoteSourceTest {

    private lateinit var sut: AuthRemoteSource
    private lateinit var json: Json

    private val apiService = mockk<AuthApiService>()

    @Before
    fun setUp() {
        json = Json
        sut = AuthRemoteSourceImpl(json, apiService)
    }

    @Test
    fun `test resendEmailVerificationCode success`() = runTest {
        val response = BaseResponse(true, "success", 200)
        coEvery { apiService.resendVerificationCode(any()) } returns response

        sut.resendEmailVerificationCode()

        coVerify { apiService.resendVerificationCode(any()) }
    }

    @Test
    fun `test resendMobilePhoneVerificationCode success`() = runTest {
        val response = BaseResponse(true, "success", 200)
        coEvery { apiService.resendVerificationCode(any()) } returns response

        sut.resendMobilePhoneVerificationCode()

        coVerify { apiService.resendVerificationCode(any()) }
    }

    @Test
    fun `test verifyChangeEmail success`() = runTest {
        coJustRun { apiService.verifyChangeEmail(any(), any()) }

        sut.verifyChangeEmail("token", "123")

        coVerify { apiService.verifyChangeEmail(any(), any()) }
    }

    @Test
    fun `test verifyChangePhone success`() = runTest {
        coJustRun { apiService.verifyChangePhone(any(), any()) }

        sut.verifyChangePhone("token", "123")

        coVerify { apiService.verifyChangePhone(any(), any()) }
    }
}
