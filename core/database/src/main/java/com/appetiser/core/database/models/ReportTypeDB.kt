package com.appetiser.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appetiser.core.domain.report.ReportType


@Entity(tableName = ReportTypeDB.REPORT_TABLE_NAME)
data class ReportTypeDB(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "type")
    var type: String? = ""
) {
    companion object {
        const val REPORT_TABLE_NAME = "report"

        fun fromDomain(reportType: ReportType): ReportTypeDB = ReportTypeDB(
            type = reportType.type
        )

        fun toDomain(reportTypeDB: ReportTypeDB): ReportType? {
            val type = reportTypeDB.type
            return ReportType.fromValue(type.orEmpty())
        }
    }
}
