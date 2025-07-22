package com.appetiser.core.domain.user

enum class UsernameType(
    val type: String
) {

    UNKNOWN("unknown"),
    EMAIL("email"),
    PHONE_NUMBER("phone_number"),
    SOCIAL("social")
}
