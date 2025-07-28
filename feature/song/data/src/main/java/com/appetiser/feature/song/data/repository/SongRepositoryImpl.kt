package com.appetiser.feature.song.data.repository

import com.appetiser.core.network.SessionProvider
import com.appetiser.core.network.utils.toBearerToken
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.network.model.payload.SongPayload
import com.appetiser.feature.song.network.remote.SongRemoteSource
import javax.inject.Inject

internal class SongRepositoryImpl @Inject constructor(
    val songRemoteSource: SongRemoteSource,
    val sessionProvider: SessionProvider
) : SongRepository {

    override suspend fun getSongs(payload: SongPayload): List<Song> {
        return songRemoteSource.getSongs(sessionProvider.getAccessToken().toBearerToken(), payload)
    }

    override suspend fun getSongDetails(id: Long): Song {
        return songRemoteSource.getSongDetails(sessionProvider.getAccessToken().toBearerToken(), id)
    }

    override suspend fun getRelatedSong(id: Long): List<Song> {
        return songRemoteSource.getRelatedSong(sessionProvider.getAccessToken().toBearerToken(), id)
    }
}
