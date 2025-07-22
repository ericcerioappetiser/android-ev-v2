package com.appetiser.core.domain.media

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Media(
    val id: Int,
    val name: String,
    val fileName: String,
    val collectionName: String,
    val mimeType: String,
    val size: Int,
    val createdAt: Instant,
    val url: String,
    val thumbUrl: String,
    val responsiveUrl: String
)
