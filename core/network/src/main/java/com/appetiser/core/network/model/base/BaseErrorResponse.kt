package com.appetiser.core.network.model.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseErrorResponse<T>(
    @SerialName("meta") val meta: T,
    @SerialName("message") val message: String,
    @SerialName("http_status") val httpStatusCode: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("error_code") val errorCode: String
)
