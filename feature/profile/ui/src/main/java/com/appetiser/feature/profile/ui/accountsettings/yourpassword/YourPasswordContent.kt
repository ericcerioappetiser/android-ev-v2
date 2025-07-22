package com.appetiser.feature.profile.ui.accountsettings.yourpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateTextButton
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.auth.ui.newpassword.ProfilePasswordField
import com.appetiser.feature.profile.ui.R

@Composable
fun YourPasswordContent(
    title: String,
    description: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    hasPasswordError: Boolean,
    onForgotClick: () -> Unit,
    onPasswordSubmit: () -> Unit,
    passwordValid: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = BaseplateTheme.colors.textPrimary,
            style = BaseplateTheme.typography.title1
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        Text(
            text = description,
            color = BaseplateTheme.colors.textSecondary,
            style = BaseplateTheme.typography.bodyRegular
        )
        Spacer(modifier = Modifier.height(Paddings.X_LARGE))
        ProfilePasswordField(
            password = password,
            onPasswordChange = onPasswordChange,
            isError = hasPasswordError
        )
        if (hasPasswordError) {
            Text(
                text = stringResource(R.string.incorrect_password),
                modifier = Modifier.padding(start = Paddings.LARGE, top = Paddings.SMALL),
                color = BaseplateTheme.colors.semanticErrorDefault,
                style = BaseplateTheme.typography.captionRegular
            )
        }
        Spacer(modifier = Modifier.weight(1.0F))
        BaseplateTextButton(
            onClick = onForgotClick,
            contentPadding = PaddingValues(vertical = Paddings.LARGE)
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                modifier = Modifier.fillMaxWidth(),
                color = BaseplateTheme.colors.primaryDefault,
                textAlign = TextAlign.Center,
                style = BaseplateTheme.typography.bodyBold
            )
        }
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        BaseplateButton(
            text = stringResource(R.string.continue_string),
            onClick = onPasswordSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = passwordValid
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YourPasswordContentPreview() {
    YourPasswordContent(
        title = stringResource(R.string.your_password),
        description = stringResource(R.string.to_change_your_email_type),
        password = "",
        onPasswordChange = {},
        hasPasswordError = false,
        onForgotClick = {},
        onPasswordSubmit = {},
        passwordValid = false,
        modifier = Modifier.padding(Paddings.LARGE)
    )
}
