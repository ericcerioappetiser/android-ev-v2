package com.appetiser.core.common.compose.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.ui.BaseplateTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    datePickerState: DatePickerState,
    onDateSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit,
    datePickerColors: DatePickerColors = DatePickerDefaults.colors(
        containerColor = BaseplateTheme.colors.textWhite,
        titleContentColor = BaseplateTheme.colors.primaryDefault,
        headlineContentColor = BaseplateTheme.colors.primaryDefault,
        subheadContentColor = BaseplateTheme.colors.primaryDefault,
        dividerColor = BaseplateTheme.colors.borderDefault,
        selectedDayContainerColor = BaseplateTheme.colors.primaryDefault,
        selectedDayContentColor = BaseplateTheme.colors.textWhite,
        todayContentColor = BaseplateTheme.colors.primaryDefault,
        dayContentColor = BaseplateTheme.colors.textPrimary,
        weekdayContentColor = BaseplateTheme.colors.textPrimary,
        yearContentColor = BaseplateTheme.colors.textPrimary,
        selectedYearContentColor = BaseplateTheme.colors.textWhite,
        selectedYearContainerColor = BaseplateTheme.colors.primaryDefault,
        disabledSelectedYearContentColor = BaseplateTheme.colors.primaryDefault
    ),
    negativeText: String = stringResource(id = R.string.cancel).uppercase(),
    positiveText: String = stringResource(id = R.string.ok).uppercase(),
    positiveStyle: TextStyle = BaseplateTheme.typography.subHeadSemiBold,
    negativeStyle: TextStyle = BaseplateTheme.typography.subHeadSemiBold,
    positiveTextColor: Color = BaseplateTheme.colors.primaryDefault,
    negativeTextColor: Color = BaseplateTheme.colors.primaryDefault

) {
    DatePickerDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis)
                    }
                    onDismissRequest()
                }
            ) {
                Text(positiveText, style = positiveStyle, color = positiveTextColor)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(negativeText, style = negativeStyle, color = negativeTextColor)
            }
        },
        content = {
            DatePicker(state = datePickerState, colors = datePickerColors)
        },
        colors = datePickerColors
    )
}

// PastOrPresentSelectableDates.kt
@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= System.currentTimeMillis()

    override fun isSelectableYear(year: Int): Boolean {
        val currentYear = Clock.System.todayIn(TimeZone.currentSystemDefault()).year
        return year <= currentYear
    }
}
