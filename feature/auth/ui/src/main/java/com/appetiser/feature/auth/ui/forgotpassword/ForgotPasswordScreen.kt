package com.appetiser.feature.auth.ui.forgotpassword

import androidx.activity.compose.BackHandler
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost
import com.appetiser.core.common.compose.components.snackbar.showBaseplateErrorSnackbar
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.auth.ui.R
import com.appetiser.feature.auth.ui.newpassword.NewPasswordContent
import com.appetiser.feature.auth.ui.verifyusername.VerifyUsernameContent
import com.appetiser.core.common.compose.R as CommonComposeR

@Composable
fun ForgotPasswordScreen(
    toPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<ForgotPasswordViewModel>()
    val viewState by viewModel.collectAsState()

    ForgotPasswordScreen(
        viewState = viewState,
        toPreviousScreen = toPreviousScreen,
        toPreviousContent = {
            viewModel.action(ForgotPasswordViewModel.Action.ShowPreviousPage)
        },
        onCodeChange = { index, char ->
            viewModel.action(
                ForgotPasswordViewModel.Action.ChangeCode(
                    index = index,
                    char = char
                )
            )
        },
        onCodeComplete = {
            viewModel.action(ForgotPasswordViewModel.Action.SubmitCode)
        },
        onResendClick = {
            viewModel.action(ForgotPasswordViewModel.Action.ResendCode)
        },
        onPasswordChange = {
            viewModel.action(
                ForgotPasswordViewModel.Action.ChangePassword(
                    password = it
                )
            )
        },
        onPasswordSubmit = {
            viewModel.action(ForgotPasswordViewModel.Action.SubmitPassword)
        },
        modifier = modifier
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewState: ForgotPasswordViewModel.ViewState,
    toPreviousScreen: () -> Unit,
    toPreviousContent: () -> Unit,
    onCodeChange: (Int, String) -> Unit,
    onCodeComplete: (String) -> Unit,
    onResendClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hostState = remember { SnackbarHostState() }

    ConsumableSideEffect(viewState.passwordUpdated) { passwordUpdated ->
        if (passwordUpdated) {
            toPreviousScreen()
        }
    }

    ConsumableSideEffect(viewState.error) {
        hostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError()
        )
    }

    BackHandler(
        enabled = viewState.page != ForgotPasswordPage.VERIFY_EMAIL,
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
                        onClick = if (viewState.page == ForgotPasswordPage.VERIFY_EMAIL) {
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
        ForgotPasswordContent(
            page = viewState.page,
            email = viewState.email,
            code = viewState.code,
            onCodeChange = onCodeChange,
            onCodeComplete = onCodeComplete,
            hasCodeError = viewState.hasCodeError,
            onResendClick = onResendClick,
            password = viewState.password,
            onPasswordChange = onPasswordChange,
            onPasswordSubmit = onPasswordSubmit,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        )
    }
}

@Composable
fun ForgotPasswordContent(
    page: ForgotPasswordPage,
    email: String,
    code: List<String>,
    onCodeChange: (Int, String) -> Unit,
    onCodeComplete: (String) -> Unit,
    hasCodeError: Boolean,
    onResendClick: () -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onPasswordSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(page) {
        focusRequester.requestFocus()
    }

    when (page) {
        ForgotPasswordPage.VERIFY_EMAIL -> VerifyUsernameContent(
            title = stringResource(R.string.verification_code),
            description = stringResource(R.string.enter_the_code_sent_to),
            username = email,
            code = code,
            onCodeChange = onCodeChange,
            onCodeComplete = onCodeComplete,
            hasCodeError = hasCodeError,
            onResendClick = onResendClick,
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(vertical = Paddings.LARGE)
        )
        ForgotPasswordPage.NEW_PASSWORD -> NewPasswordContent(
            title = stringResource(R.string.new_password),
            description = stringResource(R.string.enter_your_new_email_password),
            password = password,
            onPasswordChange = onPasswordChange,
            buttonText = stringResource(R.string.confirm_new_password),
            onPasswordSubmit = onPasswordSubmit,
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(Paddings.LARGE),
            email = email
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenPreview() {
    BaseplateTheme {
        ForgotPasswordScreen(
            viewState = ForgotPasswordViewModel.ViewState(),
            toPreviousScreen = {},
            toPreviousContent = {},
            onCodeChange = { index, char -> },
            onCodeComplete = {},
            onResendClick = {},
            onPasswordChange = {},
            onPasswordSubmit = {}
        )
    }
}
