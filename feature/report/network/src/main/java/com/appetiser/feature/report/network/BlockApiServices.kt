package com.appetiser.feature.report.network

import com.appetiser.feature.report.network.block.BlockUsersResponse
import com.appetiser.core.network.model.base.BaseResponse
import com.appetiser.core.network.model.base.BaseResponseNew
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BlockApiServices {

    @POST("users/{userId}/block")
    suspend fun blockUser(
        @Path("userId") userId: Long
    ): BaseResponse

    @DELETE("users/{userId}/block")
    suspend fun unblockUser(
        @Path("userId") userId: Long
    ): BaseResponse

    @GET("blocked?filter[type]=users")
    suspend fun getAllBlockedUsers(): BaseResponseNew<List<BlockUsersResponse>>
}
