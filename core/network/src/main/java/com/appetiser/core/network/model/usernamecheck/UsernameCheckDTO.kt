package com.appetiser.core.network.model.usernamecheck

import com.appetiser.core.domain.usernamecheck.AuthType
import com.appetiser.core.domain.usernamecheck.UsernameCheck
import com.appetiser.core.domain.usernamecheck.UsernameType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsernameCheckDTO(
    @SerialName("username") val userName: String,
    @SerialName("username_type") val userNameType: UsernameType,
    @SerialName("auth_type") val authType: AuthType,
    @SerialName("has_password") val hasPassword: Boolean
)

fun UsernameCheckDTO.toDomain() = UsernameCheck(
    userName = userName,
    userNameType = userNameType,
    authType = authType,
    hasPassword = hasPassword
)
