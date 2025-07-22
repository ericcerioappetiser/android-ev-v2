package com.appetiser.feature.auth.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterByEmailRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("password_confirmation") val passwordConfirmation: String
)
