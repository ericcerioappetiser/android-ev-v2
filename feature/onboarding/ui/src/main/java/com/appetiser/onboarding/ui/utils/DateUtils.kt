package com.appetiser.onboarding.ui.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.LocalDate as JavaLocalDate

object DateUtils {

    const val DATE_FORMAT_PATTERN = "dd/MM/yyyy"
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN, Locale.getDefault())

    fun formatMillisToDateString(millis: Long?): String = millis?.let {
        val date = Instant.fromEpochMilliseconds(it)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .toJavaLocalDate()
        dateFormatter.format(date)
    } ?: ""

    fun convertStringToLocalDate(dateString: String): LocalDate? = try {
        JavaLocalDate.parse(dateString, dateFormatter)
            .toKotlinLocalDate()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
