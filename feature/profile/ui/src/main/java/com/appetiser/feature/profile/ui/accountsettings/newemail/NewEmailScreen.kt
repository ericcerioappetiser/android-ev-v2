package com.appetiser.feature.profile.ui.accountsettings.newemail

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
import com.appetiser.feature.profile.ui.accountsettings.yourpassword.YourPasswordContent
import com.appetiser.core.common.compose.R as CommonComposeR

@Composable
fun NewEmailScreen(
    toPreviousScreen: () -> Unit,
    toForgotPassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<NewEmailViewModel>()
    val viewState by viewModel.collectAsState()

    NewEmailScreen(
        viewState = viewState,
        toPreviousScreen = toPreviousScreen,
        toPreviousContent = {
            viewModel.action(NewEmailViewModel.Action.ShowPreviousPage)
        },
        toForgotPassword = toForgotPassword,
        onPasswordChange = {
            viewModel.action(
                NewEmailViewModel.Action.ChangePassword(
                    password = it
                )
            )
        },
        onForgotClick = {
            viewModel.action(NewEmailViewModel.Action.ForgotPassword)
        },
        onPasswordSubmit = {
            viewModel.action(NewEmailViewModel.Action.SubmitPassword)
        },
        onEmailChange = {
            viewModel.action(
                NewEmailViewModel.Action.ChangeEmail(
                    email = it
                )
            )
        },
        onEmailSubmit = {
            viewModel.action(NewEmailViewModel.Action.SubmitEmail)
        },
        onCodeChange = { index, char ->
            viewModel.action(
                NewEmailViewModel.Action.ChangeCode(
                    index = index,
                    char = char
                )
            )
        },
        onCodeComplete = {
            viewModel.action(NewEmailViewModel.Action.SubmitCode)
        },
        onResendClick = {},
        modifier = modifier
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEmailScreen(
    viewState: NewEmailViewModel.ViewState,
    toPreviousScreen: () -> Unit,
    toPreviousContent: () -> Unit,
    toForgotPassword: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotClick: () -> Unit,
    onPasswordSubmit: () -> Unit,
    onEmailChange: (String) -> Unit,
    onEmailSubmit: () -> Unit,
    onCodeChange: (Int, String) -> Unit,
    onCodeComplete: (String) -> Unit,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hostState = remember { SnackbarHostState() }

    ConsumableSideEffect(viewState.passwordForgot) { email ->
        toForgotPassword(email)
    }

    ConsumableSideEffect(viewState.emailUpdated) { emailUpdated ->
        if (emailUpdated) {
            toPreviousScreen()
        }
    }

    ConsumableSideEffect(viewState.error) {
        hostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError()
        )
    }

    BackHandler(
        enabled = viewState.page != NewEmailPage.YOUR_PASSWORD,
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
                        onClick = if (viewState.page == NewEmailPage.YOUR_PASSWORD) {
                            toPreviousScreen
                        } else {
                            toPreviousContent
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(CommonComposeR.drawable.ic_back),
                            contentDescription = stringResource(CommonComposeR.string.navigate_up),
                            tint = BaseplateTheme.colors.iconDefault
                        )
                    }
                }
            )
        },
        snackbarHost = {
            BaseplateSnackbarHost(hostState = hostState)
        }
    ) { padding ->
        NewEmailPager(
            page = viewState.page,
            password = viewState.password,
            onPasswordChange = onPasswordChange,
            onForgotClick = onForgotClick,
            onPasswordSubmit = onPasswordSubmit,
            passwordValid = viewState.passwordValid,
            hasPasswordError = viewState.hasPasswordError,
            email = viewState.email,
            onEmailChange = onEmailChange,
            onEmailSubmit = onEmailSubmit,
            emailValid = viewState.emailValid,
            hasEmailError = viewState.hasEmailError,
            code = viewState.code,
            onCodeChange = onCodeChange,
            onCodeComplete = onCodeComplete,
            onResendClick = onResendClick,
            hasCodeError = viewState.hasCodeError,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        )
    }
}

@Composable
fun NewEmailPager(
    page: NewEmailPage,
    password: String,
    onPasswordChange: (String) -> Unit,
    onForgotClick: () -> Unit,
    onPasswordSubmit: () -> Unit,
    passwordValid: Boolean,
    hasPasswordError: Boolean,
    email: String,
    onEmailChange: (String) -> Unit,
    onEmailSubmit: () -> Unit,
    emailValid: Boolean,
    hasEmailError: Boolean,
    code: List<String>,
    onCodeChange: (Int, String) -> Unit,
    onCodeComplete: (String) -> Unit,
    onResendClick: () -> Unit,
    hasCodeError: Boolean,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(page) {
        focusRequester.requestFocus()
    }

    when (page) {
        NewEmailPage.YOUR_PASSWORD -> YourPasswordContent(
            title = stringResource(R.string.your_password),
            description = stringResource(R.string.to_change_your_email_type),
            password = password,
            onPasswordChange = onPasswordChange,
            hasPasswordError = hasPasswordError,
            onForgotClick = onForgotClick,
            onPasswordSubmit = onPasswordSubmit,
            passwordValid = passwordValid,
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(Paddings.LARGE)
        )
        NewEmailPage.NEW_EMAIL -> NewEmailContent(
            email = email,
            onEmailChange = onEmailChange,
            onEmailSubmit = onEmailSubmit,
            emailValid = emailValid,
            hasEmailError = hasEmailError,
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(Paddings.LARGE)
        )
        NewEmailPage.VERIFY_EMAIL -> VerifyUsernameContent(
            title = stringResource(R.string.verify_new_email),
            description = stringResource(R.string.enter_the_code_sent_to),
            username = email,
            code = code,
            onCodeChange = onCodeChange,
            onCodeComplete = onCodeComplete,
            onResendClick = onResendClick,
            hasCodeError = hasCodeError,
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(vertical = Paddings.LARGE)
        )
    }
}

@Composable
fun NewEmailContent(
    email: String,
    onEmailChange: (String) -> Unit,
    onEmailSubmit: () -> Unit,
    emailValid: Boolean,
    hasEmailError: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.new_email_address),
            color = BaseplateTheme.colors.textPrimary,
            style = BaseplateTheme.typography.title1
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        Text(
            text = stringResource(R.string.enter_your_new_email_address),
            color = BaseplateTheme.colors.textSecondary,
            style = BaseplateTheme.typography.bodyRegular
        )
        Spacer(modifier = Modifier.height(Paddings.X_LARGE))
        BaseplateTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(R.string.email_address),
                    style = BaseplateTheme.typography.subHeadRegular
                )
            },
            isError = hasEmailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            unfocusedLabelColor = BaseplateTheme.colors.textSecondary
        )
        if (hasEmailError) {
            Text(
                text = stringResource(R.string.the_email_has_already_been),
                modifier = Modifier.padding(start = Paddings.LARGE, top = Paddings.SMALL),
                color = BaseplateTheme.colors.semanticErrorDefault,
                style = BaseplateTheme.typography.captionRegular
            )
        }
        Spacer(modifier = Modifier.weight(1.0F))
        BaseplateButton(
            text = stringResource(R.string.continue_string),
            onClick = onEmailSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = emailValid
        )
    }
}

@Preview
@Composable
private fun NewEmailScreenPreview() {
    BaseplateTheme {
        NewEmailScreen(
            viewState = NewEmailViewModel.ViewState(),
            toPreviousScreen = {},
            toPreviousContent = {},
            toForgotPassword = {},
            onPasswordChange = {},
            onForgotClick = {},
            onPasswordSubmit = {},
            onEmailChange = {},
            onEmailSubmit = {},
            onCodeChange = { _, _ -> },
            onCodeComplete = {},
            onResendClick = {}
        )
    }
}
