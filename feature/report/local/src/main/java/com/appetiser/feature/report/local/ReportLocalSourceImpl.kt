package com.appetiser.feature.report.local

import androidx.room.withTransaction
import com.appetiser.core.database.AppDatabase
import com.appetiser.core.domain.report.ReportType
import com.appetiser.core.database.dao.ReportDao
import com.appetiser.core.database.models.ReportTypeDB
import javax.inject.Inject

class ReportLocalSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val reportDao: ReportDao,
) : ReportLocalSource {

    override suspend fun saveReportType(type: ReportType) {
        appDatabase.withTransaction {
            reportDao.saveReportType(type = ReportTypeDB.fromDomain(reportType = type))
        }
    }

    override suspend fun getReportType(): ReportType? = reportDao.getReportType()?.let { reportTypeDb ->
        ReportTypeDB.toDomain(reportTypeDb)
    }

    override suspend fun deleteAllReports() {
        reportDao.deleteAllReports()
    }
}
