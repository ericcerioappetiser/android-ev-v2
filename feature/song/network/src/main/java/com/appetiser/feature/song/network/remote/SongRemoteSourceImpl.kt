package com.appetiser.feature.song.network.remote

import com.appetiser.core.network.model.base.BaseRemoteSource
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.network.SongApiServices
import com.appetiser.feature.song.network.model.dto.toDomain
import com.appetiser.feature.song.network.model.payload.SongPayload
import javax.inject.Inject

internal class SongRemoteSourceImpl @Inject constructor(
    val songApiServices: SongApiServices
) : BaseRemoteSource(),
    SongRemoteSource {

    override suspend fun getSongs(
        token: String,
        payload: SongPayload
    ): List<Song> {
        return songApiServices
            .getSongs(
                token,
                payload.toQueryMap()
            ).data
            .map {
                it.toDomain()
            }
    }

    override suspend fun getSongDetails(
        token: String,
        id: Long
    ): Song {
        return songApiServices
            .getSongDetails(
                token,
                id
            ).data.toDomain()
    }

    override suspend fun getRelatedSong(
        token: String,
        id: Long
    ): List<Song> {
        return songApiServices
            .getRelatedSongs(
                token,
                id
            ).data
            .map {
                it.toDomain()
            }
    }
}
