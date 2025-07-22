@file:OptIn(ExperimentalMaterial3Api::class)

package com.appetiser.core.common.compose.components.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime


/**
 * A composable function that displays a date range picker with start and end date inputs.
 * Tapping on either input opens a modal dialog to select a date using a [DatePicker].
 *
 * This follows baseplate design guidelines for date range pickers.
 *
 * @param startPickerState The [DatePickerState] for the start date. This state is managed externally
 * and allows for observing and mutating the selected start date.
 * @param endPickerState The [DatePickerState] for the end date. This state is managed externally
 * and allows for observing and mutating the selected end date.
 * @param startDateLabel The label displayed for the start date input field.
 * @param endDateLabel The label displayed for the end date input field.
 * @param modifier The [Modifier] to be applied to the overall date range picker component.
 */
@Composable
fun BaseplateRangeDatePicker(
    startPickerState: DatePickerState,
    endPickerState: DatePickerState,
    startDateLabel: String,
    endDateLabel: String,
    modifier: Modifier = Modifier
) {
    var currentOpenDialog: CurrentOpenDialog? by remember { mutableStateOf(null) }

    Box(
        modifier = modifier.border(
            width = 1.dp,
            color = BaseplateTheme.colors.borderDefault,
            shape = RoundedCornerShape(Radius.SMALL)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Paddings.LARGE, vertical = Paddings.MICRO)
        ) {
            DateInputRow(
                modifier = Modifier.horizontalDivider(BaseplateTheme.colors.borderDefault),
                label = startDateLabel,
                date = startPickerState.selectedDate,
                onInputClick = {
                    currentOpenDialog = CurrentOpenDialog.START
                }
            )
            DateInputRow(
                label = endDateLabel,
                date = endPickerState.selectedDate,
                onInputClick = {
                    currentOpenDialog = CurrentOpenDialog.END
                }
            )
        }
    }

    if (currentOpenDialog != null) {
        val state = if (currentOpenDialog == CurrentOpenDialog.START) startPickerState else endPickerState
        DatePickerModal(
            title = if (currentOpenDialog == CurrentOpenDialog.START) startDateLabel else endDateLabel,
            state = state,
            onDateSelected = {
                state.selectedDateMillis = it
            },
            onDismiss = {
                currentOpenDialog = null
            }
        )
    }
}

@Composable
private fun DateInputRow(
    label: String,
    date: LocalDate?,
    onInputClick: () -> Unit,
    modifier: Modifier = Modifier,
    format: DateTimeFormat<LocalDate> = remember {
        LocalDate.Format {
            dayOfMonth()
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            year()
        }
    },
    defaultEmptyLabel: String = DefaultEmptyLabel
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = BaseplateTheme.typography.bodyRegular
        )
        Box(
            modifier = Modifier
                .width(InputWidthSize) // hardcoded from design
                .padding(vertical = Paddings.MEDIUM)
                .background(color = BaseplateTheme.colors.surfaceFieldTags, shape = RoundedCornerShape(Radius.SMALL))
                .clip(RoundedCornerShape(Radius.SMALL))
                .clickable(onClick = onInputClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = Paddings.SMALL),
                text = date?.format(format) ?: defaultEmptyLabel,
                style = BaseplateTheme.typography.bodyRegular
            )
        }
    }
}

@Composable
private fun DatePickerModal(
    state: DatePickerState,
    title: String,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(state.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(
            title = {
                Text(
                    modifier = Modifier.padding(
                        PaddingValues(start = Paddings.X_LARGE, end = Paddings.MEDIUM, top = Paddings.LARGE)
                    ),
                    text = title
                )
            },
            state = state
        )
    }
}

private fun Modifier.horizontalDivider(
    color: Color,
    height: Dp = 1.dp,
    start: DrawScope.() -> Offset = { Offset(x = 0f, y = size.height) },
    end: DrawScope.() -> Offset = { Offset(x = size.width, y = size.height) }
) = drawBehind {
    drawLine(
        color = color,
        strokeWidth = height.toPx(),
        start = start(),
        end = end(),
    )
}

private enum class CurrentOpenDialog {
    START,
    END
}

private val InputWidthSize = 120.dp
private const val DefaultEmptyLabel = "-"

@Preview(showBackground = true)
@Composable
private fun BaseplateRangeDatePickerPreview() {
    BaseplateTheme {
        BaseplateRangeDatePicker(
            startPickerState = rememberDatePickerState(),
            endPickerState = rememberDatePickerState(),
            startDateLabel = "Departure Date",
            endDateLabel = "Return Date"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateInputRowPreview() {
    BaseplateTheme {
        DateInputRow(
            label = "Label",
            date = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            onInputClick = {}
        )
    }
}

