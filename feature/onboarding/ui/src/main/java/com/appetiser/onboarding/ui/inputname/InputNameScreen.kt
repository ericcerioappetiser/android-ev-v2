package com.appetiser.onboarding.ui.inputname

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateCircularIndicator
import com.appetiser.core.common.compose.components.BaseplateProgressBar
import com.appetiser.core.common.compose.components.BaseplateTextField
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost
import com.appetiser.core.common.compose.components.snackbar.showBaseplateErrorSnackbar
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.onboarding.ui.R

@Composable
fun InputNameScreen(
    modifier: Modifier = Modifier,
    progress: Float = 0.35f,
    viewModel: InputNameViewModel = hiltViewModel(),
    onNameInputDone: () -> Unit = {},
) {
    val state by viewModel.collectAsState()

    InputNameScreen(
        modifier = modifier,
        progress = progress,
        state = state,
        onUpdateFirstName = {
            viewModel.action(InputNameViewModel.InputAction.UpdateFirstName(it))
        },
        onUpdateLastName = {
            viewModel.action(InputNameViewModel.InputAction.UpdateLastName(it))
        },
        onSubmit = {
            viewModel.action(InputNameViewModel.InputAction.Submit)
        },
        onNameInputDone = onNameInputDone
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun InputNameScreen(
    modifier: Modifier = Modifier,
    progress: Float = 0.35f,
    state: InputNameViewModel.InputViewState,
    onUpdateFirstName: (String) -> Unit = {},
    onUpdateLastName: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    onNameInputDone: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    ConsumableSideEffect(state.error) {
        snackbarHostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError(),
        )
    }

    ConsumableSideEffect(state.shouldNavigateToYourBirthdayScreen) {
        onNameInputDone()
    }

    Scaffold(
        snackbarHost = {
            BaseplateSnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            if (state.isLoading) {
                BaseplateCircularIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .zIndex(1f)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = Paddings.LARGE, end = Paddings.LARGE)
            ) {
                Spacer(modifier = Modifier.height(Paddings.X_LARGE))
                Text(
                    text = stringResource(id = R.string.welcome_to_app_name),
                    style = BaseplateTheme.typography.title1,
                    modifier = Modifier.fillMaxWidth(),
                )

                val focusManger = LocalFocusManager.current
                Spacer(modifier = Modifier.height(Margins.LARGE))
                val height = Modifier.height(Margins.SMALL)
                BaseplateProgressBar(
                    modifier = height,
                    progress = progress,
                )

                Spacer(modifier = Modifier.height(Margins.LARGE))
                Text(
                    text = stringResource(id = R.string.lets_add_your_complete_name),
                    style = BaseplateTheme.typography.bodyRegular,
                )
                Spacer(modifier = Modifier.height(Margins.X_LARGE))

                BaseplateTextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    value = state.firstNameInput,
                    label = {
                        Text(
                            text = stringResource(id = R.string.first_name),
                            style = BaseplateTheme.typography.footnoteRegular
                        )
                    },
                    onValueChange = onUpdateFirstName,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManger.moveFocus(FocusDirection.Down)
                        },
                    )
                )
                Spacer(modifier = Modifier.height(Margins.LARGE))
                BaseplateTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.lastNameInput,
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.last_name),
                            style = BaseplateTheme.typography.footnoteRegular
                        )
                    },
                    onValueChange = onUpdateLastName,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManger.clearFocus()
                    })
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            BaseplateButton(
                modifier = Modifier
                    .padding()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = Paddings.SMALL, end = Paddings.SMALL, bottom = Paddings.MEDIUM),
                enabled = state.isSubmitEnabled,
                onClick = onSubmit,
                text = stringResource(id = R.string.continue_text),
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun InputNameScreenPreview() {
    BaseplateTheme(isDarkTheme = false) {
        InputNameScreen(
            state = InputNameViewModel.InputViewState()
        )
    }
}
