package com.appetiser.feature.report.network

import com.appetiser.core.network.model.base.BaseResponse
import com.appetiser.core.network.model.base.BaseResponseNew
import com.appetiser.feature.report.network.models.ReportCategoryDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface ReportApiServices {

    @GET("report/categories")
    suspend fun getReportCategories(
        @Query("filter[type]") type: String
    ): BaseResponseNew<List<ReportCategoryDTO>>

    @Multipart
    @POST("report")
    suspend fun submitReport(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part attachments: MutableList<MultipartBody.Part>? = null
    ): BaseResponse

    @Multipart
    @POST("issues")
    suspend fun submitUserReportedIssue(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part attachments: MutableList<MultipartBody.Part>? = null
    ): BaseResponse
}
