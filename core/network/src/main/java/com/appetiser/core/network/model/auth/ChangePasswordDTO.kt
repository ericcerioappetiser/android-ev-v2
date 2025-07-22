package com.appetiser.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordDTO(

    @SerialName("old_password")
    val oldPassword: String,

    @SerialName("new_password")
    val newPassword: String,

    @SerialName("new_password_confirmation")
    val newPasswordConfirmation: String

)
