package com.appetiser.core.network.model.verification

import kotlinx.serialization.Serializable

@Serializable
data class GetVerificationTokenResponse(
    val token: String
)
