package com.appetiser.feature.report.local

import com.appetiser.core.domain.report.ReportType

interface ReportLocalSource {

    suspend fun saveReportType(type: ReportType)

    suspend fun getReportType(): ReportType?

    suspend fun deleteAllReports()
}
