package com.appetiser.core.network.model.user

import com.appetiser.core.network.model.base.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthDataResponse(
    @SerialName("data") val data: UserDataResponse
) : BaseResponse()
