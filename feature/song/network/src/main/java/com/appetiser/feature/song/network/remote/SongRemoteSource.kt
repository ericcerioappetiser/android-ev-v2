package com.appetiser.feature.song.network.remote

import com.appetiser.core.network.model.base.BasePagedResponse
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.network.model.dto.SongDTO
import com.appetiser.feature.song.network.model.payload.SongPayload

interface SongRemoteSource {

    suspend fun getSongs(token: String, payload: SongPayload): BasePagedResponse<SongDTO>

    suspend fun getSongDetails(token: String, id: Long): Song

    suspend fun getRelatedSong(token: String, id: Long): List<Song>
}
