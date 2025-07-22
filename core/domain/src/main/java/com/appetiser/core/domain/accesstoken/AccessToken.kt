package com.appetiser.core.domain.accesstoken

import kotlinx.serialization.Serializable

@Serializable
data class AccessToken(
    val token: String = "",
    val refresh: String = "",
    val tokenType: String = "",
    val expiresIn: String = ""
) {

    val bearerToken get() = "Bearer $token"
}
