package com.appetiser.core.network.model.base

import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
open class BaseRemoteSource {

    fun getJsonRequestBody(jsonString: String) =
        jsonString.toRequestBody("application/json".toMediaTypeOrNull())

    inline fun <reified K, reified V> StringFormat.mapToJson(map: Map<K, V>): String = encodeToString(map)
}
