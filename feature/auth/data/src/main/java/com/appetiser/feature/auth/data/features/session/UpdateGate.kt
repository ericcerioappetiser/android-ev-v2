package com.appetiser.feature.auth.data.features.session

import kotlinx.serialization.Serializable

@Serializable
data class UpdateGate(
    val id: Int,
    val version: String,
    val isRequired: Boolean,
    val title: String,
    val message: String,
    val storeUrl: String
) {
    companion object {
        const val EMPTY_ID = -1

        fun empty(): UpdateGate = UpdateGate(
            id = EMPTY_ID,
            version = "",
            isRequired = false,
            title = "",
            message = "",
            storeUrl = ""
        )
    }
}
