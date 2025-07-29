package com.appetiser.feature.song.network

import com.appetiser.core.network.model.base.BasePagedResponse
import com.appetiser.core.network.model.base.BaseResponse
import com.appetiser.core.network.model.base.BaseResponseNew
import com.appetiser.feature.song.network.model.dto.SongDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface SongApiServices {

    @GET("tracks")
    suspend fun getSongs(
        @Header("Authorization") token: String,
        @QueryMap params: Map<String, @JvmSuppressWildcards Any>
    ): BasePagedResponse<SongDTO>

    @GET("tracks/{track_id}")
    suspend fun getSongDetails(
        @Header("Authorization") token: String,
        @Path("track_id") trackId: Long
    ): BaseResponseNew<SongDTO>

    @GET("tracks/{track_id}/related")
    suspend fun getRelatedSongs(
        @Header("Authorization") token: String,
        @Path("track_id") trackId: Long
    ): BaseResponseNew<List<SongDTO>>

    @POST("tracks/{track_id}/favorite")
    suspend fun addSongToFavorite(
        @Header("Authorization") token: String,
        @Path("track_id") trackId: Long
    ): BaseResponse

    @POST("tracks/{track_id}/unfavorite")
    suspend fun unFavoriteSong(
        @Header("Authorization") token: String,
        @Path("track_id") trackId: Long
    ): BaseResponse
}
