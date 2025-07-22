package com.appetiser.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appetiser.core.database.models.ReportTypeDB

@Dao
interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReportType(type: ReportTypeDB): Long

    @Query("SELECT * FROM ${ReportTypeDB.REPORT_TABLE_NAME} LIMIT 1")
    suspend fun getReportType(): ReportTypeDB?

    @Query("DELETE FROM ${ReportTypeDB.REPORT_TABLE_NAME}")
    suspend fun deleteAllReports()
}
