package com.appetiser.feature.profile.ui.accountsettings.newphone

import androidx.activity.compose.BackHandler
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateTextField
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost
import com.appetiser.core.common.compose.components.snackbar.showBaseplateErrorSnackbar
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.auth.ui.verifyusername.VerifyUsernameContent
import com.appetiser.feature.profile.ui.R
import com.appetiser.core.common.compose.R as CommonComposeR

@Composable
fun NewPhoneScreen(
    toPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<NewPhoneViewModel>()
    val viewState by viewModel.collectAsState()

    NewPhoneScreen(
        viewState = viewState,
        toPreviousScreen = toPreviousScreen,
        toPreviousContent = {
            viewModel.action(NewPhoneViewModel.Action.ShowPreviousPage)
        },
        onCurrentCodeChange = { index, char ->
            viewModel.action(
                NewPhoneViewModel.Action.ChangeCurrentCode(
                    index = index,
                    char = char
                )
            )
        },
        onCurrentCodeComplete = {
            viewModel.action(NewPhoneViewModel.Action.SubmitCurrentCode)
        },
        onResendClick = {
            viewModel.action(
                NewPhoneViewModel.Action.ResendCode(
                    isCurrent = it
                )
            )
        },
        onPhoneChange = {
            viewModel.action(
                NewPhoneViewModel.Action.ChangeNewPhone(
                    phone = it
                )
            )
        },
        onPhoneSubmit = {
            viewModel.action(NewPhoneViewModel.Action.SubmitNewPhone)
        },
        onNewCodeChange = { index, char ->
            viewModel.action(
                NewPhoneViewModel.Action.ChangeNewCode(
                    index = index,
                    char = char
                )
            )
        },
        onNewCodeComplete = {
            viewModel.action(NewPhoneViewModel.Action.SubmitNewCode)
        },
        modifier = modifier
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPhoneScreen(
    viewState: NewPhoneViewModel.ViewState,
    toPreviousScreen: () -> Unit,
    toPreviousContent: () -> Unit,
    onCurrentCodeChange: (Int, String) -> Unit,
    onCurrentCodeComplete: (String) -> Unit,
    onResendClick: (isCurrent: Boolean) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPhoneSubmit: () -> Unit,
    onNewCodeChange: (Int, String) -> Unit,
    onNewCodeComplete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hostState = remember { SnackbarHostState() }

    ConsumableSideEffect(viewState.phoneUpdated) { phoneUpdated ->
        if (phoneUpdated) {
            toPreviousScreen()
        }
    }

    ConsumableSideEffect(viewState.error) {
        hostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError()
        )
    }

    BackHandler(
        enabled = viewState.page != NewPhonePage.VERIFY_CURRENT_PHONE,
        onBack = toPreviousContent
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = if (viewState.page == NewPhonePage.VERIFY_CURRENT_PHONE) {
                            toPreviousScreen
                        } else {
                            toPreviousContent
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(CommonComposeR.drawable.ic_back),
                            contentDescription = stringResource(CommonComposeR.string.navigate_up)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            BaseplateSnackbarHost(hostState = hostState)
        }
    ) { padding ->
        NewPhonePager(
            page = viewState.page,
            currentPhone = viewState.currentPhone,
            currentCode = viewState.currentCode,
            onCurrentCodeChange = onCurrentCodeChange,
            onCurrentCodeComplete = onCurrentCodeComplete,
            hasCurrentCodeError = viewState.hasCurrentCodeError,
            onResendClick = onResendClick,
            newPhone = viewState.newPhone,
            onPhoneChange = onPhoneChange,
            hasNewPhoneError = viewState.hasNewPhoneError,
            onPhoneSubmit = onPhoneSubmit,
            newPhoneValid = viewState.newPhoneValid,
            newCode = viewState.newCode,
            onNewCodeChange = onNewCodeChange,
            onNewCodeComplete = onNewCodeComplete,
            hasNewCodeError = viewState.hasNewCodeError,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        )
    }
}

@Composable
fun NewPhonePager(
    page: NewPhonePage,
    currentPhone: String,
    currentCode: List<String>,
    onCurrentCodeChange: (Int, String) -> Unit,
    onCurrentCodeComplete: (String) -> Unit,
    hasCurrentCodeError: Boolean,
    onResendClick: (isCurrent: Boolean) -> Unit,
    newPhone: String,
    onPhoneChange: (String) -> Unit,
    hasNewPhoneError: Boolean,
    onPhoneSubmit: () -> Unit,
    newPhoneValid: Boolean,
    newCode: List<String>,
    onNewCodeChange: (Int, String) -> Unit,
    onNewCodeComplete: (String) -> Unit,
    hasNewCodeError: Boolean,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(page) {
        focusRequester.requestFocus()
    }

    when (page) {
        NewPhonePage.VERIFY_CURRENT_PHONE -> VerifyUsernameContent(
            title = stringResource(R.string.verify_phone_number),
            description = stringResource(R.string.to_change_your_phone_number),
            username = currentPhone,
            code = currentCode,
            onCodeChange = onCurrentCodeChange,
            onCodeComplete = onCurrentCodeComplete,
            hasCodeError = hasCurrentCodeError,
            onResendClick = {
                onResendClick(true)
            },
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(vertical = Paddings.LARGE)
        )
        NewPhonePage.NEW_PHONE -> NewPhoneContent(
            phone = newPhone,
            onPhoneChange = onPhoneChange,
            hasPhoneError = hasNewPhoneError,
            onPhoneSubmit = onPhoneSubmit,
            phoneValid = newPhoneValid,
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(Paddings.LARGE)
        )
        NewPhonePage.VERIFY_NEW_PHONE -> VerifyUsernameContent(
            title = stringResource(R.string.verify_new_phone),
            description = stringResource(R.string.enter_the_code_sent_to),
            username = newPhone,
            code = newCode,
            onCodeChange = onNewCodeChange,
            onCodeComplete = onNewCodeComplete,
            hasCodeError = hasNewCodeError,
            onResendClick = {
                onResendClick(false)
            },
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(vertical = Paddings.LARGE)
        )
    }
}

@Composable
fun NewPhoneContent(
    phone: String,
    onPhoneChange: (String) -> Unit,
    hasPhoneError: Boolean,
    onPhoneSubmit: () -> Unit,
    phoneValid: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.add_phone_number),
            color = BaseplateTheme.colors.textPrimary,
            style = BaseplateTheme.typography.title1
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        Text(
            text = stringResource(R.string.add_your_new_phone_number),
            color = BaseplateTheme.colors.textSecondary,
            style = BaseplateTheme.typography.bodyRegular
        )
        Spacer(modifier = Modifier.height(Paddings.X_LARGE))
        BaseplateTextField(
            value = phone,
            onValueChange = onPhoneChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(R.string.phone_number),
                    style = BaseplateTheme.typography.subHeadRegular
                )
            },
            isError = hasPhoneError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            singleLine = true,
            unfocusedLabelColor = BaseplateTheme.colors.textSecondary
        )
        if (hasPhoneError) {
            Text(
                text = stringResource(R.string.this_phone_has_already_been_taken),
                modifier = Modifier.padding(start = Paddings.LARGE, top = Paddings.SMALL),
                color = BaseplateTheme.colors.semanticErrorDefault,
                style = BaseplateTheme.typography.captionRegular
            )
        }
        Spacer(modifier = Modifier.weight(1.0F))
        BaseplateButton(
            text = stringResource(R.string.continue_string),
            onClick = onPhoneSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = phoneValid
        )
    }
}

@Preview
@Composable
private fun NewPhoneScreenPreview() {
    BaseplateTheme {
        NewPhoneScreen(
            viewState = NewPhoneViewModel.ViewState(),
            toPreviousScreen = {},
            toPreviousContent = {},
            onCurrentCodeChange = { index, char -> },
            onCurrentCodeComplete = {},
            onResendClick = {},
            onPhoneChange = {},
            onPhoneSubmit = {},
            onNewCodeChange = { index, char -> },
            onNewCodeComplete = {}
        )
    }
}
