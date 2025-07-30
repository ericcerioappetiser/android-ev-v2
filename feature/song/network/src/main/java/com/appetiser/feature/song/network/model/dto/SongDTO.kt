package com.appetiser.feature.song.network.model.dto

import com.appetiser.feature.song.domain.models.Song
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongDTO(
    @SerialName("track_id")
    val id: Int = 0,
    @SerialName("track_name")
    val title: String? = "",
    @SerialName("long_description")
    val longDescription: String? = "",
    @SerialName("short_description")
    val shortDescription: String? = "",
    @SerialName("track_price")
    val price: Double? = 0.0,
    @SerialName("artist_name")
    val artistName: String? = "",
    @SerialName("artist_view_url")
    val artistUrl: String? = "",
    @SerialName("artwork_url100")
    val thumbnail: String? = "",
    @SerialName("preview_url")
    val videoUrl: String? = "",
    @SerialName("primary_genre_name")
    val genre: String? = "",
    @SerialName("is_favorite")
    val isFavorite: Boolean? = false

)

fun SongDTO.toDomain(): Song {
    return with(this) {
        Song(
            id = this.id,
            title = this.title.orEmpty(),
            longDescription = this.longDescription.orEmpty(),
            shortDescription = this.shortDescription.orEmpty(),
            price = this.price ?: 0.0,
            artistName = this.artistName.orEmpty(),
            artistUrl = this.artistUrl.orEmpty(),
            thumbnail = this.thumbnail.orEmpty(),
            genre = this.genre.orEmpty(),
            videoUrl = this.videoUrl.orEmpty()
        )
    }
}

