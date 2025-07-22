package com.appetiser.onboarding.ui.yourbirthday

import androidx.activity.compose.BackHandler
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateCircularIndicator
import com.appetiser.core.common.compose.components.BaseplateCustomizableButtonText
import com.appetiser.core.common.compose.components.BaseplateProgressBar
import com.appetiser.core.common.compose.components.CenterTopBar
import com.appetiser.core.common.compose.components.CustomDatePickerDialog
import com.appetiser.core.common.compose.components.PastOrPresentSelectableDates
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost
import com.appetiser.core.common.compose.components.snackbar.showBaseplateErrorSnackbar
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.onboarding.ui.R
import com.appetiser.onboarding.ui.utils.DateUtils
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun YourBirthdayScreen(
    modifier: Modifier = Modifier,
    progress: Float = 0.75f,
    viewModel: YourBirthdayViewModel = hiltViewModel(),
    onBirthdayInputDone: () -> Unit,
    onShouldExitAppOnBack: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val state by viewModel.collectAsState()

    YourBirthdayScreen(
        modifier = modifier,
        progress = progress,
        state = state,
        onClickBack = {
            viewModel.action(YourBirthdayViewModel.ActionState.OnClickBackButton)
        },
        onDateSelected = { millis ->
            viewModel.action(
                YourBirthdayViewModel.ActionState.GetSelectedDate(
                    selectedDate = DateUtils.formatMillisToDateString(
                        millis
                    )
                )
            )
        },
        onDismissDatePicker = {
            viewModel.action(YourBirthdayViewModel.ActionState.ClickDismissDatePicker)
        },
        onClickDateOfBirth = {
            viewModel.action(YourBirthdayViewModel.ActionState.ClickDateOfBirth)
        },
        onFieldValueChanged = {
            viewModel.action(YourBirthdayViewModel.ActionState.GetSelectedDate(it))
        },
        onClickContinue = {
            viewModel.action(YourBirthdayViewModel.ActionState.ClickContinue)
        },
        onBirthdayInputDone = onBirthdayInputDone,
        onShouldExitAppOnBack = onShouldExitAppOnBack,
        onNavigateUp = onNavigateUp
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourBirthdayScreen(
    modifier: Modifier = Modifier,
    progress: Float = 0.75f,
    state: YourBirthdayViewModel.YourBirthdayViewState,
    onClickBack: () -> Unit = { },
    onDateSelected: (Long) -> Unit = { },
    onDismissDatePicker: () -> Unit = { },
    onClickDateOfBirth: () -> Unit = { },
    onFieldValueChanged: (String) -> Unit = { },
    onClickContinue: () -> Unit = { },
    onBirthdayInputDone: () -> Unit = { },
    onShouldExitAppOnBack: () -> Unit = { },
    onNavigateUp: () -> Unit = { }
) {
    BackHandler {
        onClickBack()
    }
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.now()
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli(),
            selectableDates = PastOrPresentSelectableDates

        )

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var isActive by remember { mutableStateOf(false) }

    ConsumableSideEffect(state.shouldNavigateToUploadProfileScreen) {
        onBirthdayInputDone()
    }

    ConsumableSideEffect(consumable = state.shouldExitAppOnBack) {
        onShouldExitAppOnBack()
    }
    ConsumableSideEffect(consumable = state.shouldNavigateUp) {
        onNavigateUp()
    }
    ConsumableSideEffect(state.error) {
        snackbarHostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError(),
        )
    }

    Scaffold(
        snackbarHost = {
            BaseplateSnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Paddings.X_LARGE),
                title = "",
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = BaseplateTheme.colors.iconDefault
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                BaseplateCircularIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = Margins.LARGE, end = Margins.LARGE)
            ) {
                Spacer(modifier = Modifier.height(Margins.X_LARGE))
                Text(
                    text = stringResource(id = R.string.your_birthday),
                    style = BaseplateTheme.typography.title1,
                    modifier = Modifier.fillMaxWidth(),
                )

                val height = Modifier.height(Margins.SMALL)
                Spacer(modifier = Modifier.height(Margins.LARGE))
                BaseplateProgressBar(
                    modifier = height,
                    progress = progress,
                )
                Spacer(modifier = Modifier.height(Margins.LARGE))
                Text(
                    text = stringResource(id = R.string.please_enter_your_date_of_birth),
                    style = BaseplateTheme.typography.bodyRegular,
                )
                if (state.showDialog) {
                    CustomDatePickerDialog(
                        datePickerState = datePickerState,
                        onDateSelected = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                onDateSelected(millis)
                            }
                        },
                        onDismissRequest = onDismissDatePicker
                    )
                }
                Spacer(modifier = Modifier.height(Margins.X_LARGE))
                BaseplateCustomizableButtonText(
                    isActive = isActive,
                    onClick = {
                        isActive = it
                        onClickDateOfBirth()
                    },
                    label = stringResource(id = R.string.date_of_birth),
                    fieldValue = state.initialDate,
                    modifier = Modifier.fillMaxWidth(),
                    onFieldValueChanged = onFieldValueChanged
                )
            }


            BaseplateButton(
                modifier = Modifier
                    .padding()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = Paddings.LARGE, end = Paddings.LARGE, bottom = Paddings.MEDIUM),
                enabled = state.isContinueEnabled,
                onClick = onClickContinue,
                text = stringResource(id = R.string.continue_text)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun YourBirthdayScreenPreview() {
    BaseplateTheme(isDarkTheme = false) {
        YourBirthdayScreen(
            state = YourBirthdayViewModel.YourBirthdayViewState()
        )
    }
}
