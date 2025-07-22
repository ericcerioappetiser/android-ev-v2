package com.appetiser.feature.profile.ui.accountsettings.deleteaccount

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.appetiser.core.domain.user.UsernameType
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.auth.ui.verifyusername.VerifyUsernameContent
import com.appetiser.feature.profile.ui.R
import com.appetiser.feature.profile.ui.accountsettings.yourpassword.YourPasswordContent
import com.appetiser.core.common.compose.R as CommonComposeR

@Composable
fun DeleteAccountScreen(
    toPreviousScreen: () -> Unit,
    toForgotPassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<DeleteAccountViewModel>()
    val viewState by viewModel.collectAsState()

    DeleteAccountScreen(
        viewState = viewState,
        toPreviousScreen = toPreviousScreen,
        onPasswordChange = {
            viewModel.action(
                DeleteAccountViewModel.Action.ChangePassword(
                    password = it
                )
            )
        },
        onForgotClick = {
            toForgotPassword(viewState.email)
        },
        onPasswordSubmit = {
            viewModel.action(DeleteAccountViewModel.Action.SubmitPassword)
        },
        onCodeChange = { index, char ->
            viewModel.action(
                DeleteAccountViewModel.Action.ChangeCode(
                    index = index,
                    char = char
                )
            )
        },
        onCodeComplete = {
            viewModel.action(DeleteAccountViewModel.Action.SubmitCode)
        },
        onResendClick = {
            viewModel.action(DeleteAccountViewModel.Action.ResendCode)
        },
        modifier = modifier
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(
    viewState: DeleteAccountViewModel.ViewState,
    toPreviousScreen: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotClick: () -> Unit,
    onPasswordSubmit: () -> Unit,
    onCodeChange: (Int, String) -> Unit,
    onCodeComplete: (String) -> Unit,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hostState = remember { SnackbarHostState() }

    ConsumableSideEffect(viewState.accountDeleted) { accountDeleted ->
        if (accountDeleted) {
            toPreviousScreen()
        }
    }

    ConsumableSideEffect(viewState.error) {
        hostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError()
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = toPreviousScreen) {
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
        DeleteAccountContent(
            usernameType = viewState.usernameType,
            password = viewState.password,
            onPasswordChange = onPasswordChange,
            hasPasswordError = viewState.hasPasswordError,
            onForgotClick = onForgotClick,
            onPasswordSubmit = onPasswordSubmit,
            passwordValid = viewState.passwordValid,
            phone = viewState.phone,
            code = viewState.code,
            onCodeChange = onCodeChange,
            onCodeComplete = onCodeComplete,
            hasCodeError = viewState.hasCodeError,
            onResendClick = onResendClick,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        )
    }
}

@Composable
fun DeleteAccountContent(
    usernameType: UsernameType,
    password: String,
    onPasswordChange: (String) -> Unit,
    hasPasswordError: Boolean,
    onForgotClick: () -> Unit,
    onPasswordSubmit: () -> Unit,
    passwordValid: Boolean,
    phone: String,
    code: List<String>,
    onCodeChange: (Int, String) -> Unit,
    onCodeComplete: (String) -> Unit,
    hasCodeError: Boolean,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (usernameType) {
        UsernameType.EMAIL -> YourPasswordContent(
            title = stringResource(R.string.delete_account_1),
            description = stringResource(R.string.to_delete_your_account_please),
            password = password,
            onPasswordChange = onPasswordChange,
            hasPasswordError = hasPasswordError,
            onForgotClick = onForgotClick,
            onPasswordSubmit = onPasswordSubmit,
            passwordValid = passwordValid,
            modifier = modifier.padding(Paddings.LARGE)
        )
        UsernameType.PHONE_NUMBER ->
            VerifyUsernameContent(
                title = stringResource(R.string.delete_account_1),
                description = stringResource(R.string.to_delete_your_account_please_1),
                username = phone,
                code = code,
                onCodeChange = onCodeChange,
                onCodeComplete = onCodeComplete,
                hasCodeError = hasCodeError,
                onResendClick = onResendClick,
                modifier = modifier.padding(vertical = Paddings.LARGE)
            )
        else -> {}
    }
}

@Preview
@Composable
private fun DeleteAccountScreenPreview() {
    BaseplateTheme {
        DeleteAccountScreen(
            viewState = DeleteAccountViewModel.ViewState(
                usernameType = UsernameType.PHONE_NUMBER
            ),
            toPreviousScreen = {},
            onPasswordChange = {},
            onForgotClick = {},
            onPasswordSubmit = {},
            onCodeChange = { index, char -> },
            onCodeComplete = {},
            onResendClick = {}
        )
    }
}
