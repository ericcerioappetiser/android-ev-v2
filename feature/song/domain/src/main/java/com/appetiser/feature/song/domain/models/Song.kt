package com.appetiser.feature.song.domain.models

data class Song(
    val id: Int,
    val title: String,
    val longDescription: String,
    val shortDescription: String,
    val price: Double,
    val artistName: String,
    val artistUrl: String,
    val thumbnail: String,
    val videoUrl: String,
    val isFavorite: Boolean = false
)
