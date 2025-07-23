package com.appetiser.feature.song.domain.models

class Song(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val artistName: String,
    val thumbnail: String,
    val videoUrl: String,
    val isFavorite: Boolean = false
)
