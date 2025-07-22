package com.appetiser.core.user.network

import com.appetiser.core.network.model.base.BasePagedResponse
import com.appetiser.core.network.model.base.BaseResponseNew
import com.appetiser.core.network.model.base.PagingOption
import com.appetiser.core.network.model.media.MediaFileDTO
import com.appetiser.core.network.model.user.UpdateUserRequestDTO
import com.appetiser.core.network.model.user.UserDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface OnboardingApiService {

    @GET("auth/profile")
    suspend fun getCurrentUser(): BaseResponseNew<UserDTO>

    @PUT("auth/profile")
    suspend fun updateCurrentUser(
        @Body body: UpdateUserRequestDTO
    ): BaseResponseNew<UserDTO>

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): UserDTO

    @PATCH("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body body: RequestBody
    )

    @Multipart
    @POST("users/{id}/avatar")
    suspend fun uploadUserAvatar(
        @Path("id") id: Int,
        @Part body: MultipartBody.Part
    ): BaseResponseNew<MediaFileDTO>

    @POST("auth/onboarding/complete")
    suspend fun onboardingComplete(): BaseResponseNew<UserDTO>

    @GET("users")
    suspend fun searchUser(
        @Query("filter[search]") query: String,
        @QueryMap pagingOption: PagingOption,
    ): BasePagedResponse<UserDTO>
}
