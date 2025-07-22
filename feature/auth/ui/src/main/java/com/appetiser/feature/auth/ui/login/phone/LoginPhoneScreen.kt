package com.appetiser.feature.auth.ui.login.phone

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.android.util.TimeUtils.formatToMinutesAndSeconds
import com.appetiser.core.common.compose.components.BaseplateCustomInputScreen
import com.appetiser.core.common.compose.components.BaseplateOtpInput
import com.appetiser.core.common.compose.components.BaseplateTextButton
import com.appetiser.core.common.compose.components.CenterTopBar
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.auth.ui.login.phone.LoginPhoneViewModel.ActionState.OnOtpComplete
import com.appetiser.feature.auth.ui.login.phone.LoginPhoneViewModel.ActionState.OnOtpInput
import com.appetiser.feature.auth.ui.login.phone.LoginPhoneViewModel.ActionState.OnResendClick
import com.appetiser.core.common.compose.R as commonR
import com.appetiser.feature.auth.ui.R as authR

@Composable
fun LoginPhoneScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: LoginPhoneViewModel = hiltViewModel(),
    onLoginPhoneSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.collectAsState()

    LoginPhoneScreen(
        modifier = modifier,
        state = state,
        onLoginPhoneSuccess = onLoginPhoneSuccess,
        onBackClick = onBackClick,
        onOtpInput = { index, value ->
            viewModel.action(
                OnOtpInput(
                    index,
                    value
                )
            )
        },
        onOtpComplete = {
            viewModel.action(OnOtpComplete)
        },
        onResendClick = {
            viewModel.action(OnResendClick)
        },
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun LoginPhoneScreen(
    modifier: Modifier = Modifier,
    state: LoginPhoneViewModel.LoginPhoneViewState,
    onLoginPhoneSuccess: () -> Unit,
    onBackClick: () -> Unit,
    onOtpInput: (index: Int, value: String) -> Unit,
    onOtpComplete: () -> Unit,
    onResendClick: () -> Unit,
) {
    var errorMessage by remember { mutableStateOf("") }

    ConsumableSideEffect(state.onLoginSuccess) {
        onLoginPhoneSuccess()
    }

    ConsumableSideEffect(state.error) {
        errorMessage = it.getThrowableError()
    }

    BaseplateCustomInputScreen(
        modifier = modifier,
        headerContent = {
            Spacer(modifier = Modifier.height(Paddings.MICRO))

            Text(
                text = stringResource(id = authR.string.welcome_back),
                style = BaseplateTheme.typography.title2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Paddings.MEDIUM))

            Text(
                text = stringResource(id = authR.string.enter_the_code_sent_to),
                style = BaseplateTheme.typography.bodyRegular,
                color = BaseplateTheme.colors.textSecondary
            )

            Spacer(modifier = Modifier.height(Paddings.MICRO))

            Text(
                text = state.phoneNumber,
                style = BaseplateTheme.typography.bodySemiBold,
                color = BaseplateTheme.colors.textSecondary
            )
        },
        topBarContent = {
            CenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = commonR.drawable.ic_back),
                            contentDescription = stringResource(id = authR.string.back),
                            tint = BaseplateTheme.colors.textPrimary,
                        )
                    }
                }
            )
        },
        content = {
            BaseplateOtpInput(
                modifier = Modifier.fillMaxWidth(),
                otpLength = state.otp.size,
                otpValues = state.otp,
                onOtpChange = onOtpInput
            ) {
                onOtpComplete()
            }

            Spacer(modifier = Modifier.height(Paddings.MICRO))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.fillMaxWidth(),
                    color = BaseplateTheme.colors.semanticErrorDefault,
                    style = BaseplateTheme.typography.bodyRegular,
                    textAlign = TextAlign.Center
                )
            }
        },

        footerContent = {
            val isResendEnabled = state.timer == null
            BaseplateTextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Paddings.SMALL,
                        end = Paddings.SMALL,
                        bottom = Paddings.X_LARGE
                    ),
                enabled = isResendEnabled,
                onClick = {
                    onResendClick()
                },
                text = state.timer?.toLong()?.formatToMinutesAndSeconds()?.let {
                    stringResource(
                        id = authR.string.resend_code_in_time_format,
                        it
                    )
                } ?: stringResource(id = authR.string.resend)
            )
        }
    )
}

@Composable
@Preview
fun LoginPhoneScreenPreview() {
    BaseplateTheme {
        LoginPhoneScreen(
            state = LoginPhoneViewModel.LoginPhoneViewState(),
            onLoginPhoneSuccess = { },
            onBackClick = { },
            onOtpInput = { index, value -> },
            onOtpComplete = {},
            onResendClick = {}
        )
    }
}
