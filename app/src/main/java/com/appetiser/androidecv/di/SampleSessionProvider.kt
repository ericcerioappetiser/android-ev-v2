package com.appetiser.androidecv.di

import com.appetiser.core.network.SessionProvider
import com.appetiser.feature.auth.data.SessionRepository
import javax.inject.Inject

class SampleSessionProvider @Inject constructor(
    private val sessionRepository: SessionRepository
) : SessionProvider {

    override suspend fun getUserId(): Long =
        sessionRepository.getSession().user.id.toLong()

    override suspend fun getAccessToken(): String =
        sessionRepository.getSession().accessToken.token
}
