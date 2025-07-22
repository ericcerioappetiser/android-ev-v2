package com.appetiser.feature.auth.ui.login

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateTextButton
import com.appetiser.core.common.compose.components.BaseplateTextField
import com.appetiser.core.common.compose.components.CenterTopBar
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost
import com.appetiser.core.common.compose.components.snackbar.showBaseplateErrorSnackbar
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.auth.ui.R
import com.appetiser.feature.auth.ui.utils.AuthState


@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    toForgotPassword: (String) -> Unit,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToVerifyScreen: (String) -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToBirthdayScreen: () -> Unit,
    onNavigateToAddNameScreen: () -> Unit
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManger = LocalFocusManager.current

    ConsumableSideEffect(state.error) {
        snackbarHostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError(),
        )
    }
    ConsumableSideEffect(state.shouldNavigateToMainScreen) {
        onNavigateToHomeScreen()
    }
    ConsumableSideEffect(consumable = state.authState) {
        when (it) {
            AuthState.Completed -> {
                onNavigateToHomeScreen()
            }
            AuthState.NoBirthDate -> {
                onNavigateToBirthdayScreen()
            }
            AuthState.NoFullName -> {
                onNavigateToAddNameScreen()
            }
            AuthState.NoProfilePhoto -> {
                onNavigateToProfileScreen()
            }
            AuthState.NotVerified -> {
                onNavigateToVerifyScreen(state.user?.username.orEmpty())
            }
        }
    }
    LoginScreen(
        snackbarHostState = snackbarHostState,
        state = state,
        onBackClick = onBackClick,
        toForgotPassword = toForgotPassword,
        onAction = {
            viewModel.action(it)
        }
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    toForgotPassword: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    state: LoginViewModel.LoginViewState,
    onAction: (LoginViewModel.Action) -> Unit
) {
    ConsumableSideEffect(state.error) {
        snackbarHostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError(),
        )
    }
    val focusManger = LocalFocusManager.current

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CenterTopBar(
                    modifier = modifier,
                    navigationIcon = {
                        IconButton(onClick = { onBackClick.invoke() }) {
                            Icon(
                                painterResource(id = com.appetiser.core.common.compose.R.drawable.ic_back),
                                contentDescription = stringResource(R.string.back),
                                tint = BaseplateTheme.colors.textPrimary,
                            )
                        }
                    },
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Paddings.LARGE)
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_back),
                        style = BaseplateTheme.typography.title2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = modifier.height(Paddings.LARGE))
                    Text(
                        text = stringResource(id = R.string.enter_your_password_login_to),
                        style = BaseplateTheme.typography.bodyRegular,
                        color = BaseplateTheme.colors.textSecondary
                    )
                    Spacer(modifier = modifier.height(Paddings.MICRO))
                    Text(
                        text = state.email,
                        style = BaseplateTheme.typography.bodySemiBold
                    )
                    Spacer(modifier = modifier.height(Paddings.LARGE))

                    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

                    BaseplateTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.password,
                        singleLine = true,
                        label = {
                            Text(
                                text = stringResource(id = R.string.password),
                                style = BaseplateTheme.typography.footnoteRegular
                            )
                        },
                        onValueChange = { passwordInput ->
                            onAction(
                                LoginViewModel.Action.OnPasswordChange(
                                    password = passwordInput
                                )
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    painter = if (isPasswordVisible) {
                                        painterResource(id = R.drawable.ic_hide_password)
                                    } else {
                                        painterResource(
                                            id = R.drawable.ic_show_password
                                        )
                                    },
                                    contentDescription = if (isPasswordVisible) {
                                        stringResource(id = R.string.hide_password)
                                    } else {
                                        stringResource(
                                            id = R.string.show_password
                                        )
                                    }
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation(
                                mask = '\u25CF'
                            )
                        },
                        enabled = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManger.clearFocus()
                        })
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BaseplateTextButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    onClick = {
                        toForgotPassword(state.email)
                    },
                    text = stringResource(id = R.string.forgot_password),
                )
                Spacer(modifier = Modifier.height(Paddings.MEDIUM))
                BaseplateButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Paddings.LARGE, end = Paddings.LARGE, bottom = Paddings.SMALL),
                    enabled = true,
                    onClick = {
                        onAction(LoginViewModel.Action.OnLogin)
                    },
                    text = stringResource(id = R.string.btn_continue),
                )
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    BaseplateTheme {
        LoginScreen(
            onAction = {},
            onBackClick = {},
            toForgotPassword = {},
            state = LoginViewModel.LoginViewState(),
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
