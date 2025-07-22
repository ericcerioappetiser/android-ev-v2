package com.appetiser.feature.auth.data.features.session

import com.appetiser.core.domain.accesstoken.AccessToken
import com.appetiser.core.domain.user.User
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    var user: User,
    var accessToken: AccessToken,
    var updateGate: UpdateGate? = null,
    var recommendUpdateTimeStamp: Long = 0L,
    var hasSkippedEmailVerification: Boolean = false
)
