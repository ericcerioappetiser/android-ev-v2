package com.appetiser.feature.song.data.repository

import androidx.paging.PagingSource
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.network.model.payload.SongPayload

interface SongRepository {

    fun getSongs(payload: SongPayload): PagingSource<Int, Song>

    suspend fun getSongDetails(id: Long): Song

    suspend fun getRelatedSong(id: Long): List<Song>
}
