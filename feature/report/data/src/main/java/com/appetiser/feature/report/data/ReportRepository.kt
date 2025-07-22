package com.appetiser.feature.report.data

import com.appetiser.core.domain.report.ReportCategory
import com.appetiser.core.domain.report.ReportRequest
import com.appetiser.core.domain.report.ReportType
import com.appetiser.core.domain.report.UserReportRequest

interface ReportRepository {

    suspend fun getReportCategories(
        type: String
    ): List<ReportCategory>

    suspend fun submitReport(
        reportRequest: ReportRequest
    ): Boolean

    suspend fun submitUserReportedIssue(
        userReportRequest: UserReportRequest
    ): Boolean

    suspend fun getReportType(): ReportType?

    suspend fun updateLatestReportType(reportType: ReportType)

    suspend fun deleteAllReportType()
}
