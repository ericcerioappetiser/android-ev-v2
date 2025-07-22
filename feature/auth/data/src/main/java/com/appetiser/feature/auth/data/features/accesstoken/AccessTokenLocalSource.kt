package com.appetiser.feature.auth.data.features.accesstoken

import com.appetiser.core.domain.accesstoken.AccessToken
import kotlinx.coroutines.flow.Flow

interface AccessTokenLocalSource {

    suspend fun getAccessToken(): AccessToken

    fun getAccessTokenFlow(): Flow<AccessToken>

    suspend fun getAccessTokenFromPref(): String

    suspend fun saveAccessToken(accessToken: AccessToken): AccessToken

    suspend fun deleteToken()
}
