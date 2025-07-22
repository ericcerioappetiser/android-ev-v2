package com.appetiser.core.network.model.user

import com.appetiser.core.domain.user.User
import com.appetiser.core.domain.user.UsernameType
import com.appetiser.core.network.model.media.MediaFileDTO
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("id") val id: Int,
    @SerialName("full_name") val fullName: String,
    @SerialName("first_name") val firstName: String?,
    @SerialName("last_name") val lastName: String?,
    @SerialName("description") val description: String?,
    @SerialName("birthdate") val birthdate: LocalDate?,
    @SerialName("gender") val gender: String?,
    @SerialName("email") val email: String?,
    @SerialName("phone_number") val phoneNumber: String?,
    @SerialName("created_at") val createdAt: Instant?,
    @SerialName("updated_at") val updatedAt: Instant?,
    @SerialName("blocked_at") val blockedAt: Instant?,
    @SerialName("onboarded_at") val onboardedAt: Instant?,
    @SerialName("primary_username") val primaryUsername: String,
    @SerialName("email_verified") val emailVerified: Boolean,
    @SerialName("phone_number_verified") val phoneNumberVerified: Boolean,
    @SerialName("verified") val verified: Boolean,
    @SerialName("avatar_permanent_url") val avatarPermanentUrl: String,
    @SerialName("avatar_permanent_thumb_url") val avatarPermanentThumbUrl: String,
    @SerialName("mine") val mine: Boolean,
    @SerialName("avatar") val avatar: MediaFileDTO?,
    @SerialName("stripe_customer_id") val stripeCustomerId: String?
) {

    fun toDomain(): User = User(
        id = id,
        fullName = fullName,
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        description = description.orEmpty(),
        birthdate = birthdate,
        gender = gender.orEmpty(),
        email = email.orEmpty(),
        phoneNumber = phoneNumber.orEmpty(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        blockedAt = blockedAt,
        onboardedAt = onboardedAt,
        primaryUsername = UsernameType.entries.find { type ->
            type.type == primaryUsername
        } ?: UsernameType.UNKNOWN,
        emailVerified = emailVerified,
        phoneNumberVerified = phoneNumberVerified,
        verified = verified,
        avatarPermanentUrl = avatarPermanentUrl,
        avatarPermanentThumbUrl = avatarPermanentThumbUrl,
        mine = mine,
        avatar = avatar?.toDomain(), // Assuming MediaDB also has a toDomain() method
        stripeCustomerId = stripeCustomerId
    )
}
