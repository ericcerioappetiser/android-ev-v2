package com.appetiser.feature.auth.data.features.session

import android.content.SharedPreferences
import com.appetiser.core.user.local.feature.UserLocalSource
import com.appetiser.feature.auth.data.features.accesstoken.AccessTokenLocalSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SessionLocalSourceImpl @Inject constructor(
    private val userLocalSource: UserLocalSource,
    private val accessTokenLocalSource: AccessTokenLocalSource,
    private val sharedPreferences: SharedPreferences
) : SessionLocalSource {

    companion object {
        private const val PREF_RECOMMEND_UPDATE_TIMESTAMP = "PREF_RECOMMEND_UPDATE_TIMESTAMP"
        private const val PREF_HAS_SKIPPED_EMAIL_VERIFICATION =
            "PREF_HAS_SKIPPED_EMAIL_VERIFICATION"
    }

    var session: Session? = null

    override suspend fun getSession(): Session = session ?: getSessionFromDb().also { session = it }

    override fun getSessionFlow() = combine(
        userLocalSource.getUserFlow(),
        accessTokenLocalSource.getAccessTokenFlow()
    ) { user, accessToken ->
        Session(
            user = user,
            accessToken = accessToken
        )
    }

    override suspend fun saveSession(session: Session): Session {
        val (user, accessToken) = coroutineScope {
            val userDef = async { userLocalSource.saveUser(session.user) }
            val tokenDef = async { accessTokenLocalSource.saveAccessToken(session.accessToken) }
            userDef.await() to tokenDef.await()
        }
        saveSessionFlags(session = session)
        session.user = user
        session.accessToken = accessToken
        this.session = session
        return this.session!!
    }

    override suspend fun getUserToken(): String = accessTokenLocalSource.getAccessTokenFromPref()

    private suspend fun getSessionFromDb(): Session {
        val (user, token) = coroutineScope {
            val userDef = async { userLocalSource.getUser() }
            val tokenDef = async { accessTokenLocalSource.getAccessToken() }
            userDef.await() to tokenDef.await()
        }
        val session = Session(user = user, accessToken = token)
        getSessionFlags(session)
        return session
    }

    override suspend fun clearSession() {
        clearSessionFlags()

        coroutineScope {
            // Parallel decomposition
            awaitAll(
                async { userLocalSource.deleteUser() },
                async { accessTokenLocalSource.deleteToken() }
            )
        }

        this.session = null
    }

    private fun saveSessionFlags(session: Session) {
        sharedPreferences
            .edit()
            .apply {
                putLong(PREF_RECOMMEND_UPDATE_TIMESTAMP, session.recommendUpdateTimeStamp)
                putBoolean(PREF_HAS_SKIPPED_EMAIL_VERIFICATION, session.hasSkippedEmailVerification)
                apply()
            }
    }

    private fun getSessionFlags(session: Session) {
        session.recommendUpdateTimeStamp =
            sharedPreferences.getLong(PREF_RECOMMEND_UPDATE_TIMESTAMP, 0L)
        session.hasSkippedEmailVerification =
            sharedPreferences.getBoolean(PREF_HAS_SKIPPED_EMAIL_VERIFICATION, false)
    }

    private fun clearSessionFlags() {
        sharedPreferences
            .edit()
            .apply {
                putLong(PREF_RECOMMEND_UPDATE_TIMESTAMP, 0L)
                putBoolean(PREF_HAS_SKIPPED_EMAIL_VERIFICATION, false)
                apply()
            }
    }
}
