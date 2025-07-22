package com.appetiser.feature.auth.data.features.accesstoken

import android.content.SharedPreferences
import androidx.room.withTransaction
import com.appetiser.core.database.AppDatabase
import com.appetiser.core.database.models.AccessTokenDB
import com.appetiser.core.domain.accesstoken.AccessToken
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccessTokenLocalSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val appDatabase: AppDatabase
) : AccessTokenLocalSource {

    private val tokenDao by lazy {
        appDatabase.tokenDao()
    }

    companion object {
        private const val PREF_USER_TOKEN = "PREF_USER_TOKEN"
    }

    override suspend fun getAccessToken(): AccessToken {
        val accessTokenDb = tokenDao.getToken() ?: AccessTokenDB()
        return AccessTokenDB.toDomain(accessTokenDB = accessTokenDb)
    }

    override fun getAccessTokenFlow() =
        tokenDao
            .getTokenFlow()
            .map {
                val token = it ?: AccessTokenDB()
                AccessTokenDB.toDomain(token)
            }

    override suspend fun getAccessTokenFromPref(): String = sharedPreferences.getString(PREF_USER_TOKEN, "").orEmpty()

    override suspend fun saveAccessToken(accessToken: AccessToken): AccessToken = appDatabase.withTransaction {
        appDatabase.tokenDao().logoutToken()
        appDatabase.tokenDao().saveToken(AccessTokenDB.fromDomain(accessToken = accessToken))
        saveAccessTokenToPref(accessToken.token)
        accessToken
    }

    private fun saveAccessTokenToPref(accessToken: String) {
        sharedPreferences
            .edit()
            .apply {
                putString(PREF_USER_TOKEN, accessToken)
                apply()
            }
    }

    override suspend fun deleteToken() {
        saveAccessTokenToPref("")
        return tokenDao.logoutToken()
    }
}
