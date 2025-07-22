package com.appetiser.core.network.model.usernamecheck

import com.appetiser.core.domain.usernamecheck.AuthType
import com.appetiser.core.domain.usernamecheck.UsernameType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserNameCheckErrorMetaDTO(
    @SerialName("username_type") val usernameType: UsernameType,
    @SerialName("auth_type") val authType: AuthType
)
