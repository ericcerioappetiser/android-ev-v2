package com.appetiser.core.domain.user

import com.appetiser.core.domain.media.Media
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val fullName: String,
    val firstName: String,
    val lastName: String,
    val description: String,
    val birthdate: LocalDate?,
    val gender: String,
    val email: String,
    val phoneNumber: String,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val blockedAt: Instant?,
    val onboardedAt: Instant?,
    val primaryUsername: UsernameType,
    val emailVerified: Boolean,
    val phoneNumberVerified: Boolean,
    val verified: Boolean,
    val avatarPermanentUrl: String,
    val avatarPermanentThumbUrl: String,
    val mine: Boolean,
    val avatar: Media?,
    val stripeCustomerId: String?
) {
    val hasFullName: Boolean
        get() = firstName.isNotEmpty() && lastName.isNotEmpty()

    val hasEmail: Boolean
        get() = email.isNotEmpty()

    val username get() = email.takeIf { it.isNotEmpty() } ?: phoneNumber

    val hasBirthdate get() = birthdate != null

    val hasProfilePhoto: Boolean
        get() = avatar?.url?.isNotEmpty() == true


    val isEmpty: Boolean
        get() = id == 0


    val userAvatarURL: String
        get() = avatar?.thumbUrl?.ifEmpty { avatarPermanentThumbUrl }.orEmpty()

    companion object {
        fun empty() = User(
            id = 0,
            fullName = "",
            firstName = "",
            lastName = "",
            description = "",
            birthdate = null,
            gender = "",
            email = "",
            phoneNumber = "",
            createdAt = Clock.System.now(),
            updatedAt = null,
            blockedAt = null,
            onboardedAt = null,
            primaryUsername = UsernameType.UNKNOWN,
            emailVerified = false,
            phoneNumberVerified = false,
            verified = false,
            avatarPermanentUrl = "",
            avatarPermanentThumbUrl = "",
            mine = false,
            avatar = null,
            stripeCustomerId = null
        )
    }
}
