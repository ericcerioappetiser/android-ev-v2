package com.appetiser.feature.auth.data

import com.appetiser.feature.auth.data.features.session.SessionLocalSource
import com.appetiser.feature.auth.network.AuthRemoteSource
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest {

    private lateinit var sut: AuthRepository

    private val remoteSource = mockk<AuthRemoteSource>()
    private val sessionLocalSource = mockk<SessionLocalSource>()

    private val json = mockk<Json>()

    @Before
    fun setUp() {
        sut = AuthRepositoryImpl(remoteSource, sessionLocalSource, json)
    }

    // TODO: Add tests for other methods in AuthRepositoryImpl

    @Test
    fun `test resendEmailVerificationCode success`() = runTest {
        coEvery { remoteSource.resendEmailVerificationCode() } returns true

        sut.resendEmailVerificationCode()

        coVerify { remoteSource.resendEmailVerificationCode() }
    }

    @Test
    fun `test resendMobilePhoneVerificationCode success`() = runTest {
        coEvery { remoteSource.resendMobilePhoneVerificationCode() } returns true

        sut.resendMobilePhoneVerificationCode()

        coVerify { remoteSource.resendMobilePhoneVerificationCode() }
    }

    @Test
    fun `test verifyChangeEmail success`() = runTest {
        val session = Stubs.session
        coJustRun { remoteSource.verifyChangeEmail(any(), any()) }
        coEvery { sessionLocalSource.getSession() } returns session
        coEvery { remoteSource.getUser() } returns Stubs.user
        coJustRun { sessionLocalSource.saveSession(session) }

        sut.verifyChangeEmail("token", "123")

        coVerify { remoteSource.verifyChangeEmail(any(), any()) }
    }

    @Test
    fun `test verifyChangePhone success`() = runTest {
        val session = Stubs.session
        coJustRun { remoteSource.verifyChangePhone(any(), any()) }
        coEvery { sessionLocalSource.getSession() } returns session
        coEvery { remoteSource.getUser() } returns Stubs.user
        coJustRun { sessionLocalSource.saveSession(session) }

        sut.verifyChangePhone("token", "123")

        coVerify { remoteSource.verifyChangePhone(any(), any()) }
    }
}
