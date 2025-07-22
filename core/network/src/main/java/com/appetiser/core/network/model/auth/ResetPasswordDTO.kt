package com.appetiser.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordDTO(

    @SerialName("username")
    val username: String,

    @SerialName("token")
    val token: String,

    @SerialName("password")
    val password: String,

    @SerialName("password_confirmation")
    val passwordConfirmation: String

)
