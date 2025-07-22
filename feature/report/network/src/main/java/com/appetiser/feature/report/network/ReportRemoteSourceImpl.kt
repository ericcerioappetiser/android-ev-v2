package com.appetiser.feature.report.network

import com.appetiser.core.domain.report.ReportCategory
import com.appetiser.core.domain.report.ReportRequest
import com.appetiser.core.domain.report.UserReportRequest
import com.appetiser.feature.report.network.models.ReportCategoryDTO.Companion.toDomain
import com.appetiser.feature.report.network.models.ReportRequestDTO
import com.appetiser.feature.report.network.models.UserReportRequestDTO
import com.appetiser.feature.report.network.models.toMultipart
import javax.inject.Inject

class ReportRemoteSourceImpl @Inject constructor(
    private val apiService: ReportApiServices
) : ReportRemoteSource {

    override suspend fun getReportCategories(type: String): List<ReportCategory> = apiService.getReportCategories(type = type)
        .data.map { it.run { toDomain() } }

    override suspend fun submitReport(
        reportRequest: ReportRequest
    ): Boolean {
        val request = ReportRequestDTO(reportRequest).toMultipart()
        return apiService
            .submitReport(
                request.first,
                request.second
            ).success
    }

    override suspend fun submitUserReportedIssue(
        userReportRequest: UserReportRequest
    ): Boolean {
        val request = UserReportRequestDTO(userReportRequest).toMultipart()
        return apiService.submitUserReportedIssue(
            map = request.first,
            attachments = request.second
        ).success
    }
}
