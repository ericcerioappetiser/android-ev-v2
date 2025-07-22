package com.appetiser.core.database.models

import com.appetiser.core.domain.media.Media
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class MediaDB(
    private val id: Int,
    private val name: String,
    private val fileName: String,
    private val collectionName: String,
    private val mimeType: String,
    private val size: Int,
    private val createdAt: Instant,
    private val url: String,
    private val thumbUrl: String,
    private val responsiveUrl: String
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

    companion object {
        fun empty(): MediaDB = MediaDB(
            id = 0,
            name = "",
            fileName = "",
            collectionName = "",
            mimeType = "",
            size = 0,
            createdAt = Clock.System.now(),
            url = "",
            thumbUrl = "",
            responsiveUrl = ""
        )
        fun toDB(media: Media?): MediaDB = MediaDB(
            id = media?.id ?: 0,
            name = media?.name.orEmpty(),
            fileName = media?.fileName.orEmpty(),
            collectionName = media?.collectionName.orEmpty(),
            mimeType = media?.mimeType.orEmpty(),
            size = media?.size ?: 0,
            createdAt = media?.createdAt ?: Clock.System.now(),
            url = media?.url.orEmpty(),
            thumbUrl = media?.thumbUrl.orEmpty(),
            responsiveUrl = media?.responsiveUrl.orEmpty()
        )
    }
}

