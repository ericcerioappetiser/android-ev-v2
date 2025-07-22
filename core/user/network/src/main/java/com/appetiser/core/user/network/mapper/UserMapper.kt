package com.appetiser.core.user.network.mapper

import com.appetiser.core.domain.user.User
import com.appetiser.core.network.model.user.UserDTO

internal fun User.toNetwork() = UserDTO(
    id = id,
    fullName = fullName,
    firstName = firstName,
    lastName = lastName,
    description = description,
    birthdate = birthdate,
    gender = gender,
    email = email,
    phoneNumber = phoneNumber,
    createdAt = createdAt,
    updatedAt = updatedAt,
    blockedAt = blockedAt,
    onboardedAt = onboardedAt,
    primaryUsername = primaryUsername.type,
    emailVerified = emailVerified,
    phoneNumberVerified = phoneNumberVerified,
    verified = verified,
    avatarPermanentUrl = avatarPermanentUrl,
    avatarPermanentThumbUrl = avatarPermanentThumbUrl,
    mine = mine,
    avatar = null,
    stripeCustomerId = stripeCustomerId
)
