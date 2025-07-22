package com.appetiser.feature.auth.data

import com.appetiser.feature.auth.data.features.session.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun getSession(): Session

    fun getSessionFlow(): Flow<Session>

    suspend fun saveSession(session: Session): Session

    suspend fun clearSession()
}
