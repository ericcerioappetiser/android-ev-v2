package com.appetiser.feature.profile.ui.accountsettings.newpassword

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
import com.appetiser.feature.auth.ui.newpassword.NewPasswordContent
import com.appetiser.feature.profile.ui.R
import com.appetiser.feature.profile.ui.accountsettings.yourpassword.YourPasswordContent
import com.appetiser.core.common.compose.R as CommonComposeR
import com.appetiser.feature.auth.ui.R as AuthUiR

@Composable
fun NewPasswordScreen(
    toPreviousScreen: () -> Unit,
    toForgotPassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<NewPasswordViewModel>()
    val viewState by viewModel.collectAsState()

    NewPasswordScreen(
        viewState = viewState,
        toPreviousScreen = toPreviousScreen,
        toPreviousContent = {
            viewModel.action(NewPasswordViewModel.Action.ShowPreviousPage)
        },
        onCurrentPasswordChange = {
            viewModel.action(
                NewPasswordViewModel.Action.ChangeCurrentPassword(
                    password = it
                )
            )
        },
        onForgotClick = {
            toForgotPassword(viewState.email)
        },
        onCurrentPasswordSubmit = {
            viewModel.action(NewPasswordViewModel.Action.SubmitCurrentPassword)
        },
        onNewPasswordChange = {
            viewModel.action(
                NewPasswordViewModel.Action.ChangeNewPassword(
                    password = it
                )
            )
        },
        onNewPasswordSubmit = {
            viewModel.action(NewPasswordViewModel.Action.SubmitNewPassword)
        },
        modifier = modifier
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordScreen(
    viewState: NewPasswordViewModel.ViewState,
    toPreviousScreen: () -> Unit,
    toPreviousContent: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onForgotClick: () -> Unit,
    onCurrentPasswordSubmit: () -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onNewPasswordSubmit: () -> Unit,
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
        enabled = viewState.page != NewPasswordPage.YOUR_PASSWORD,
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
                        onClick = if (viewState.page == NewPasswordPage.YOUR_PASSWORD) {
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
        NewPasswordPager(
            page = viewState.page,
            currentPassword = viewState.currentPassword,
            onCurrentPasswordChange = onCurrentPasswordChange,
            hasCurrentPasswordError = viewState.hasCurrentPasswordError,
            onForgotClick = onForgotClick,
            onCurrentPasswordSubmit = onCurrentPasswordSubmit,
            currentPasswordValid = viewState.currentPasswordValid,
            newPassword = viewState.newPassword,
            onNewPasswordChange = onNewPasswordChange,
            onNewPasswordSubmit = onNewPasswordSubmit,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        )
    }
}

@Composable
fun NewPasswordPager(
    page: NewPasswordPage,
    currentPassword: String,
    onCurrentPasswordChange: (String) -> Unit,
    hasCurrentPasswordError: Boolean,
    onForgotClick: () -> Unit,
    onCurrentPasswordSubmit: () -> Unit,
    currentPasswordValid: Boolean,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    onNewPasswordSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(page) {
        focusRequester.requestFocus()
    }

    when (page) {
        NewPasswordPage.YOUR_PASSWORD -> YourPasswordContent(
            title = stringResource(R.string.your_password),
            description = stringResource(R.string.to_change_your_password_type),
            password = currentPassword,
            onPasswordChange = onCurrentPasswordChange,
            hasPasswordError = hasCurrentPasswordError,
            onForgotClick = onForgotClick,
            onPasswordSubmit = onCurrentPasswordSubmit,
            passwordValid = currentPasswordValid,
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(Paddings.LARGE)
        )
        NewPasswordPage.NEW_PASSWORD -> NewPasswordContent(
            title = stringResource(AuthUiR.string.new_password),
            description = stringResource(AuthUiR.string.enter_your_new_email_password),
            password = newPassword,
            onPasswordChange = onNewPasswordChange,
            buttonText = stringResource(R.string.continue_string),
            onPasswordSubmit = onNewPasswordSubmit,
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(Paddings.LARGE)
        )
    }
}

@Preview
@Composable
private fun NewPasswordPreview() {
    BaseplateTheme {
        NewPasswordScreen(
            viewState = NewPasswordViewModel.ViewState(
                page = NewPasswordPage.NEW_PASSWORD
            ),
            toPreviousScreen = {},
            toPreviousContent = {},
            onCurrentPasswordChange = {},
            onForgotClick = {},
            onCurrentPasswordSubmit = {},
            onNewPasswordChange = {},
            onNewPasswordSubmit = {}
        )
    }
}
