package com.appetiser.feature.auth.data

import com.appetiser.feature.auth.data.features.session.Session
import com.appetiser.feature.auth.data.features.session.SessionLocalSource
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionLocalSource: SessionLocalSource
) : SessionRepository {

    override suspend fun getSession(): Session = sessionLocalSource.getSession()

    override fun getSessionFlow() = sessionLocalSource.getSessionFlow()

    override suspend fun saveSession(session: Session): Session = sessionLocalSource.saveSession(session)

    override suspend fun clearSession() = sessionLocalSource.clearSession()
}
