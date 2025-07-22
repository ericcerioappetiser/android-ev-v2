package com.appetiser.core.network.model.media

import com.appetiser.core.domain.media.Media
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaFileDTO(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("file_name") val fileName: String,
    @SerialName("collection_name") val collectionName: String,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("size") val size: Int,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("url") val url: String,
    @SerialName("thumb_url") val thumbUrl: String,
    @SerialName("responsive_url") val responsiveUrl: String
) {
    fun toDomain(): Media = Media(
        id = id,
        name = name,
        fileName = fileName,
        collectionName = collectionName,
        mimeType = mimeType,
        size = size,
        createdAt = createdAt,
        url = url,
        thumbUrl = thumbUrl,
        responsiveUrl = responsiveUrl
    )
}
