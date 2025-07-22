package com.appetiser.feature.auth.data.features.session

import kotlinx.coroutines.flow.Flow

interface SessionLocalSource {

    suspend fun getSession(): Session

    fun getSessionFlow(): Flow<Session>

    suspend fun saveSession(session: Session): Session

    suspend fun getUserToken(): String

    suspend fun clearSession()
}
