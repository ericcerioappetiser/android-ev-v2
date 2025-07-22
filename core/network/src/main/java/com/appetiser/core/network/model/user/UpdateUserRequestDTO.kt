package com.appetiser.core.network.model.user

import com.appetiser.core.domain.user.UpdateUserRequest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequestDTO(
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("birthdate") val birthdate: LocalDate? = null,
    @SerialName("gender") val gender: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("id") val id: Int
)

fun UpdateUserRequest.toRequestDTO() = UpdateUserRequestDTO(
    firstName = firstName,
    lastName = lastName,
    birthdate = birthdate,
    gender = gender,
    description = description,
    email = email,
    id = id
)
