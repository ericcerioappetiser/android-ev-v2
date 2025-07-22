package com.appetiser.feature.report.data

import com.appetiser.core.common.android.util.dispatchers.DispatcherProvider
import com.appetiser.core.domain.report.ReportCategory
import com.appetiser.core.domain.report.ReportRequest
import com.appetiser.core.domain.report.ReportType
import com.appetiser.core.domain.report.UserReportRequest
import com.appetiser.feature.report.local.ReportLocalSource
import com.appetiser.feature.report.network.ReportRemoteSource
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteSource: ReportRemoteSource,
    private val reportLocalSource: ReportLocalSource,
    private val dispatchersProvider: DispatcherProvider
) : ReportRepository {

    override suspend fun getReportCategories(
        type: String
    ): List<ReportCategory> = reportRemoteSource
        .getReportCategories(type)

    override suspend fun submitReport(
        reportRequest: ReportRequest
    ) = withContext(dispatchersProvider.io) {
        reportRemoteSource
            .submitReport(
                reportRequest
            )
    }

    override suspend fun submitUserReportedIssue(
        userReportRequest: UserReportRequest
    ) = withContext(dispatchersProvider.io) {
        reportRemoteSource.submitUserReportedIssue(
            userReportRequest
        )
    }

    override suspend fun getReportType() = withContext(dispatchersProvider.io) {
        reportLocalSource.getReportType()
    }

    override suspend fun updateLatestReportType(reportType: ReportType) = withContext(dispatchersProvider.io) {
        reportLocalSource.deleteAllReports()
        reportLocalSource.saveReportType(type = reportType)
    }


    override suspend fun deleteAllReportType() = withContext(dispatchersProvider.io) {
        reportLocalSource.deleteAllReports()
    }
}
