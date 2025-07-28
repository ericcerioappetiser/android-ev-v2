package com.appetiser.feature.song.data.repository

import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.network.model.payload.SongPayload

interface SongRepository {

    suspend fun getSongs(payload: SongPayload): List<Song>

    suspend fun getSongDetails(id: Long): Song

    suspend fun getRelatedSong(id: Long): List<Song>
}
