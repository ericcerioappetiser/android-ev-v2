package com.appetiser.feature.auth.ui.verifyusername

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.components.BaseplateChronometer
import com.appetiser.core.common.compose.components.BaseplateOtpInput
import com.appetiser.core.common.compose.components.BaseplateTextButton
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.auth.ui.R
import kotlin.time.Duration.Companion.minutes

@Composable
fun VerifyUsernameContent(
    title: String,
    description: String,
    username: String,
    code: List<String>,
    onCodeChange: (Int, String) -> Unit,
    onCodeComplete: (String) -> Unit,
    hasCodeError: Boolean,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var resendEnabled by rememberSaveable { mutableStateOf(true) }

    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(start = Paddings.LARGE),
            color = BaseplateTheme.colors.textPrimary,
            style = BaseplateTheme.typography.title1
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        Text(
            text = description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.LARGE),
            color = BaseplateTheme.colors.textSecondary,
            style = BaseplateTheme.typography.bodyRegular
        )
        Text(
            text = username,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.LARGE),
            color = BaseplateTheme.colors.textSecondary,
            style = BaseplateTheme.typography.bodySemiBold
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        BaseplateOtpInput(
            otpValues = code,
            onOtpChange = onCodeChange,
            onOtpComplete = onCodeComplete,
            isError = hasCodeError,
            textStyle = BaseplateTheme
                .typography
                .title2
                .copy(textAlign = TextAlign.Center)
        )
        if (hasCodeError) {
            Spacer(modifier = Modifier.height(Margins.MEDIUM))
            Text(
                text = stringResource(R.string.this_code_is_invalid),
                modifier = Modifier.fillMaxWidth(),
                color = BaseplateTheme.colors.semanticErrorDefault,
                textAlign = TextAlign.Center,
                style = BaseplateTheme.typography.bodyRegular
            )
        }
        Spacer(modifier = Modifier.weight(1.0F))
        if (resendEnabled) {
            BaseplateTextButton(
                onClick = {
                    onResendClick()
                    resendEnabled = false
                },
                modifier = Modifier.padding(horizontal = Paddings.LARGE)
            ) {
                Text(
                    text = stringResource(R.string.resend),
                    modifier = Modifier.fillMaxWidth(),
                    color = BaseplateTheme.colors.primaryDefault,
                    textAlign = TextAlign.Center,
                    style = BaseplateTheme.typography.bodyBold
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Paddings.LARGE),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.resend_code_in),
                    color = BaseplateTheme.colors.textDisabled,
                    style = BaseplateTheme.typography.bodyBold
                )
                Spacer(modifier = Modifier.width(Paddings.MICRO))
                BaseplateChronometer(
                    duration = 1.minutes,
                    style = BaseplateTheme
                        .typography
                        .bodyBold
                        .copy(color = BaseplateTheme.colors.textDisabled)
                ) {
                    resendEnabled = true
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifyUsernameContentPreview() {
    BaseplateTheme {
        VerifyUsernameContent(
            title = stringResource(R.string.verify_new_email),
            description = stringResource(R.string.enter_the_code_sent_to),
            username = "jerome.capulong@appetiser.com.au",
            code = listOf("", "", "", "", ""),
            onCodeChange = { _, _ -> },
            onCodeComplete = {},
            onResendClick = {},
            hasCodeError = false,
            modifier = Modifier.padding(vertical = Paddings.LARGE)
        )
    }
}
