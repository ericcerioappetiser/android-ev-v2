package com.appetiser.core.common.compose.components.datepicker

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
var DatePickerState.selectedDate: LocalDate?
    get() = selectedDateMillis?.let {
        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
    }
    set(value) {
        selectedDateMillis = value?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds()
    }

