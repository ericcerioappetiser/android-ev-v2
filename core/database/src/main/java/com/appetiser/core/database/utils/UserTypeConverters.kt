package com.appetiser.core.database.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.appetiser.core.database.models.MediaDB
import com.appetiser.core.database.models.UserDB
import com.appetiser.core.domain.report.ReportType
import kotlinx.serialization.json.Json
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ProvidedTypeConverter
class UserTypeConverters @Inject constructor(private val json: Json) {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun userFromString(value: String): UserDB = json.decodeFromString(value)

    @TypeConverter
    fun userToString(value: UserDB): String = json.encodeToString(value)

    @TypeConverter
    fun mediaFromString(value: String): MediaDB = json.decodeFromString(value)

    @TypeConverter
    fun mediaToString(value: MediaDB): String = json.encodeToString(value)

    @TypeConverter
    fun instantFromLong(value: Long): Instant = Instant.fromEpochMilliseconds(value)

    @TypeConverter
    fun instantToString(value: Instant): Long = value.toEpochMilliseconds()

    @TypeConverter
    fun localDateToString(localDate: LocalDate): String = localDate.toString()

    @TypeConverter
    fun localDateFromString(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun reportTypeToString(reportType: ReportType?): String? = reportType?.type

    @TypeConverter
    fun stringToReportType(value: String?): ReportType? = value?.let { ReportType.fromValue(it) }
}
