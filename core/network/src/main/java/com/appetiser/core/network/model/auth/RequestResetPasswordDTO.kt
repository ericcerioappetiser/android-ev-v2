package com.appetiser.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestResetPasswordDTO(

    @SerialName("username")
    val username: String

)
