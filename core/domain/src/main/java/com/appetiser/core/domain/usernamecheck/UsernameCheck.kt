package com.appetiser.core.domain.usernamecheck

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsernameCheck(
    val userName: String,
    val userNameType: UsernameType,
    val authType: AuthType,
    val hasPassword: Boolean
)

class UsernameNotFoundException(
    message: String,
    val usernameType: UsernameType?,
    val authType: AuthType?
) : Exception(message)

@Serializable
enum class AuthType {

    @SerialName("otp")
    Otp,

    @SerialName("password")
    Password
}

@Serializable
enum class UsernameType(val value: String) {

    @SerialName("email")
    Email("email"),

    @SerialName("phone_number")
    Phone("phone_number");

    companion object {

        fun fromValue(usernameTypeString: String): UsernameType = entries.find { it.value == usernameTypeString }
            ?: throw IllegalArgumentException("Unknown username type: $usernameTypeString!")
    }
}
