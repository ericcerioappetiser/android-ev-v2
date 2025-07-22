package com.appetiser.feature.report.network

import com.appetiser.core.domain.report.ReportCategory
import com.appetiser.core.domain.report.ReportRequest
import com.appetiser.core.domain.report.UserReportRequest

interface ReportRemoteSource {

    suspend fun getReportCategories(
        type: String
    ): List<ReportCategory>

    suspend fun submitReport(
        reportRequest: ReportRequest
    ): Boolean

    suspend fun submitUserReportedIssue(
        userReportRequest: UserReportRequest
    ): Boolean
}
