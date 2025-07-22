package com.appetiser.feature.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.baseplate.common.util.PhoneUtils.isValidEmail
import com.appetiser.core.common.compose.components.BaseplateOtpInput
import com.appetiser.core.common.compose.components.CenterTopBar
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost
import com.appetiser.core.common.compose.components.snackbar.showBaseplateErrorSnackbar
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.auth.ui.VerifyOtpViewModel.Action
import com.appetiser.feature.auth.ui.VerifyOtpViewModel.VerifyOtpViewState

@Composable
fun VerifyOtpScreenRoot(
    viewModel: VerifyOtpViewModel = hiltViewModel(),
    onOtpVerified: (String) -> Unit = {},
    onUserLoggedOut: () -> Unit,
) {
    val state by viewModel.collectAsState()
    ConsumableSideEffect(state.shouldNavigateToNextScreen) {
        onOtpVerified(it)
    }
    ConsumableSideEffect(consumable = state.shouldNavigateUp) {
        onUserLoggedOut()
    }
    VerifyOtpScreen(
        onAction = { action ->
            viewModel.action(action)
        },
        state = state
    )
}

@Composable
fun VerifyOtpScreen(
    onAction: (Action) -> Unit,
    state: VerifyOtpViewState
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }


    ConsumableSideEffect(state.error) {
        snackbarHostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError(),
        )
    }
    BackHandler {
        onAction(Action.OnClickBackButton)
    }

    Scaffold(
        snackbarHost = { BaseplateSnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.union(WindowInsets.ime)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
        ) {
            CenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(Action.OnClickBackButton) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = BaseplateTheme.colors.iconDefault,
                            modifier = Modifier.padding(horizontal = Paddings.SMALL),
                        )
                    }
                }
            )
            Text(
                text = if (isValidEmail(state.username)) {
                    stringResource(R.string.verify_email)
                } else {
                    stringResource(R.string.verify_phone_number)
                },
                style = BaseplateTheme.typography.title1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.LARGE)
            )
            Text(
                text = if (isValidEmail(state.username)) {
                    stringResource(R.string.verify_email_instructions)
                } else {
                    stringResource(R.string.verify_phone_instructions)
                },
                style = BaseplateTheme.typography.bodyRegular,
                color = BaseplateTheme.colors.textSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Paddings.LARGE)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = state.username.ifEmpty {
                    state.argsUserName
                },
                style = BaseplateTheme.typography.bodySemiBold,
                color = BaseplateTheme.colors.textPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Paddings.LARGE)
            )

            Spacer(modifier = Modifier.height(12.dp))

            BaseplateOtpInput(
                modifier = Modifier.padding(vertical = Paddings.LARGE),
                otpValues = state.otpValues,
                onOtpChange = { index, value ->
                    onAction(Action.OnOtpInputChange(index, value))
                },
                onOtpComplete = {
                    onAction(Action.OnOtpVerify)
                }
            )

            if (state.isCodeInvalid) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.this_code_is_invalid),
                    color = BaseplateTheme.colors.semanticErrorDefault,
                    style = BaseplateTheme.typography.bodyRegular
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(Paddings.LARGE)
                    .clickable {
                        onAction(Action.OnResendOtp)
                    },
                text = state.timerText,
                color = if (state.isCountdownActive) BaseplateTheme.colors.textDisabled else BaseplateTheme.colors.textBrand,
            )

            Spacer(modifier = Modifier.height(Margins.XX_LARGE))
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun VerifyOtpScreenPreview() {
    BaseplateTheme(isDarkTheme = false) {
        VerifyOtpScreen(
            state = VerifyOtpViewState(timerText = stringResource(R.string.resend)),
            onAction = {}
        )
    }
}
