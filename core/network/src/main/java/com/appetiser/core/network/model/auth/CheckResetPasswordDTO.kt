package com.appetiser.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckResetPasswordDTO(

    @SerialName("username")
    val username: String,

    @SerialName("token")
    val token: String

)
