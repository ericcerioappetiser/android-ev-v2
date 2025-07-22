package com.appetiser.feature.auth.data

import com.appetiser.core.domain.accesstoken.AccessToken
import com.appetiser.core.domain.media.Media
import com.appetiser.core.domain.user.User
import com.appetiser.core.domain.user.UsernameType
import com.appetiser.feature.auth.data.features.session.Session
import com.appetiser.feature.auth.data.features.session.UpdateGate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

object Stubs {

    val user = User(
        id = 1,
        fullName = "John Doe",
        firstName = "John",
        lastName = "Doe",
        description = "A sample user",
        birthdate = LocalDate(1990, 1, 1),
        gender = "Male",
        email = "john.doe@example.com",
        phoneNumber = "+15551234567",
        createdAt = Clock.System.now(),
        updatedAt = null,
        blockedAt = null,
        onboardedAt = Clock.System.now(),
        primaryUsername = UsernameType.EMAIL,
        emailVerified = true,
        phoneNumberVerified = true,
        verified = true,
        avatarPermanentUrl = "https://example.com/avatar.jpg",
        avatarPermanentThumbUrl = "https://example.com/avatar_thumb.jpg",
        mine = true,
        avatar = Media(
            id = 1,
            name = "My Image",
            fileName = "my_image.jpg",
            collectionName = "Images",
            mimeType = "image/jpeg",
            size = 1024,
            createdAt = Clock.System.now(),
            url = "https://example.com/my_image.jpg",
            thumbUrl = "https://example.com/my_image_thumb.jpg",
            responsiveUrl = "https://example.com/my_image_responsive.jpg"
        ),
        stripeCustomerId = "cus_123"
    )

    val accessToken = AccessToken(
        token = "your_token",
        refresh = "your_refresh_token",
        tokenType = "Bearer",
        expiresIn = "3600"
    )

    val updateGate = UpdateGate(
        id = 1,
        version = "1.1.0",
        isRequired = true,
        title = "New Update",
        message = "Please update to the latest version.",
        storeUrl = "https://play.google.com/store/apps/your_app"
    )

    val session = Session(
        user = user,
        accessToken = accessToken,
        updateGate = updateGate,
        recommendUpdateTimeStamp = System.currentTimeMillis(),
        hasSkippedEmailVerification = false
    )
}
