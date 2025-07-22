package com.appetiser.core.network.model.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse(
    @SerialName("success") val success: Boolean = false,
    @SerialName("message") val message: String = "",
    @SerialName("http_status") val httpStatus: Int = 500
) {

    fun isSuccessful(): Boolean = httpStatus == 200 || httpStatus == 201
}
