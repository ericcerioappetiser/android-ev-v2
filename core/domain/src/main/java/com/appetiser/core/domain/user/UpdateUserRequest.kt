package com.appetiser.core.domain.user

import kotlinx.datetime.LocalDate

data class UpdateUserRequest(
    val fullName: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthdate: LocalDate? = null,
    val gender: String? = null,
    val description: String? = null,
    val email: String? = null,
    val id: Int
)

