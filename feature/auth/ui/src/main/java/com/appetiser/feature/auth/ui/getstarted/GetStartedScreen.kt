package com.appetiser.feature.auth.ui.getstarted

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateCircularIndicator
import com.appetiser.core.common.compose.components.BaseplateOutlinedButton
import com.appetiser.core.common.compose.components.BaseplateTextFieldValue
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost
import com.appetiser.core.common.compose.components.snackbar.showBaseplateErrorSnackbar
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.auth.ui.R
import com.appetiser.feature.auth.ui.getstarted.GetStartedScreenViewModel.ActionState
import com.appetiser.core.common.android.R as commonAndroid
import com.appetiser.core.common.compose.R as commonCompose


@Composable
fun GetStartedScreen(
    viewModel: GetStartedScreenViewModel = hiltViewModel(),
    onLoginPhone: (String) -> Unit = {},
    onLoginEmail: (String) -> Unit = {},
    onSignUpEmail: (String) -> Unit = {},
    onSignUpPhone: (String) -> Unit = {}
) {
    val state by viewModel.collectAsState()
    GetStartedScreen(
        state = state,
        onUserNameChange = { username ->
            viewModel.action(ActionState.OnUserNameChange(username))
        },
        onClickContinue = {
            viewModel.action(ActionState.OnClickContinue)
        },
    )
    ConsumableSideEffect(state.onNavigateTo) {
        when (it) {
            GetStartedScreenViewModel.NavigateDestination.LOGIN -> onLoginEmail(state.userName)
            GetStartedScreenViewModel.NavigateDestination.LOGIN_PHONE -> onLoginPhone(state.userName)
            GetStartedScreenViewModel.NavigateDestination.CREATE_PASSWORD -> onSignUpEmail(state.userName)
            GetStartedScreenViewModel.NavigateDestination.VERIFY_PHONE -> onSignUpPhone(state.formattedUsername)
        }
    }
}

@Composable
fun GetStartedScreen(
    state: GetStartedScreenViewModel.GetStartedViewState,
    onUserNameChange: (String) -> Unit = {},
    onClickContinue: () -> Unit = {},
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    ConsumableSideEffect(state.error) {
        snackbarHostState.showBaseplateErrorSnackbar(
            message = it.getThrowableError(),
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        snackbarHost = {
            BaseplateSnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                BaseplateCircularIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .zIndex(1f)
                )
            }

            Column(
                modifier = Modifier
                    .padding(Paddings.LARGE),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Paddings.XXXX_LARGE))

                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = stringResource(R.string.app_icon),
                    modifier = Modifier.size(Paddings.XXXXX_LARGE)
                )

                Spacer(modifier = Modifier.height(Paddings.LARGE))

                Text(
                    text = stringResource(R.string.app_name),
                    style = BaseplateTheme.typography.title2,
                    color = BaseplateTheme.colors.textPrimary
                )

                Spacer(modifier = Modifier.height(Paddings.X_LARGE))

                Text(
                    text = stringResource(R.string.landing_desc),
                    textAlign = TextAlign.Center,
                    style = BaseplateTheme.typography.bodyRegular,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = Paddings.X_LARGE)
                )

                Spacer(modifier = Modifier.height(Paddings.X_LARGE))

                var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

                LaunchedEffect(state.formattedUsername) {
                    if (textFieldValue.text != state.formattedUsername) {
                        textFieldValue = TextFieldValue(
                            text = state.formattedUsername,
                            selection = TextRange(state.formattedUsername.length)
                        )
                    }
                }

                BaseplateTextFieldValue(
                    value = textFieldValue,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(id = R.string.enter_email_or_number),
                            style = BaseplateTheme.typography.bodyRegular
                        )
                    },
                    onValueChange = { newValue ->
                        if (newValue.text != textFieldValue.text) { // Prevent redundant emissions
                            textFieldValue = newValue
                            onUserNameChange(newValue.text)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = if (state.isPhoneNumber) KeyboardType.Phone else KeyboardType.Email
                    ),
                    keyboardActions = KeyboardActions(
                        onAny = {
                            keyboardController?.hide()
                            onClickContinue()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(Paddings.LARGE))

                BaseplateButton(
                    shape = RoundedCornerShape(Paddings.MEDIUM),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClickContinue,
                    text = stringResource(id = R.string.btn_continue),
                    enabled = state.isValidInput && !state.isLoading
                )

                Spacer(modifier = Modifier.height(Paddings.X_LARGE))

                DividerWithText(stringResource(R.string.or))

                Spacer(modifier = Modifier.height(Paddings.X_LARGE))

                // Social Login Buttons
                SocialLoginButton(
                    text = stringResource(R.string.continue_with_google),
                    icon = R.drawable.google_icon,
                    onClick = { /* Handle Google login */ }
                )

                Spacer(modifier = Modifier.height(Paddings.MEDIUM))

                SocialLoginButton(
                    text = stringResource(R.string.continue_with_facebook),
                    icon = R.drawable.facebook_icon,
                    onClick = { /* Handle Facebook login */ }
                )

                Spacer(modifier = Modifier.height(Paddings.MEDIUM))

                SocialLoginButton(
                    text = stringResource(R.string.continue_with_apple),
                    icon = R.drawable.apple_icon,
                    onClick = { /* Handle Apple login */ }
                )

                Spacer(modifier = Modifier.weight(1f))

                val annotatedFooter = buildFooterDisclaimer(context)

                ClickableText(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = annotatedFooter,
                    style = BaseplateTheme.typography.subHeadRegular,
                ) { offset ->
                    annotatedFooter.getStringAnnotations(
                        tag = context.getString(R.string.tag_terms),
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        /* Open terms of service */
                    }

                    annotatedFooter.getStringAnnotations(
                        tag = context.getString(R.string.tag_privacy),
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        /* Open privacy policy */
                    }
                }


                Spacer(modifier = Modifier.height(Paddings.SMALL))

                Text(
                    text = stringResource(
                        R.string.version,
                        context.packageManager.getPackageInfo(
                            LocalContext.current.packageName,
                            0
                        )?.versionName.orEmpty()
                    ),
                    style = BaseplateTheme.typography.footnoteRegular,
                    color = BaseplateTheme.colors.textSecondary
                )
            }
        }
    }
}

@Composable
fun DividerWithText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HorizontalDivider(
            color = BaseplateTheme.colors.borderDefault,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )

        Text(
            modifier = Modifier.padding(start = Paddings.SMALL, end = Paddings.SMALL),
            style = BaseplateTheme.typography.footnoteRegular,
            text = text,
            color = BaseplateTheme.colors.textPrimary
        )

        HorizontalDivider(
            color = BaseplateTheme.colors.borderDefault,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SocialLoginButton(
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    BaseplateOutlinedButton(
        shape = RoundedCornerShape(Paddings.MEDIUM),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(
            1.dp,
            color = BaseplateTheme.colors.borderDefault
        )
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(Paddings.X_LARGE)
        )
        Spacer(modifier = Modifier.width(Paddings.MEDIUM))
        Text(text, style = BaseplateTheme.typography.bodyBold)
    }
}

private fun buildFooterDisclaimer(context: Context): AnnotatedString = buildAnnotatedString {
    append(context.getString(R.string.footer_disclaimer))
    append(context.getString(commonAndroid.string.space))

    // "Terms of Service" - bold and clickable
    pushStringAnnotation(
        tag = context.getString(R.string.tag_terms),
        annotation = context.getString(R.string.tag_terms)
    )

    withStyle(style = SpanStyle(fontFamily = FontFamily(Font(commonCompose.font.urbanist_bold)))) {
        append(context.getString(R.string.terms))
    }
    pop()

    append(context.getString(R.string.and))

    // "Privacy Policy" - bold and clickable
    pushStringAnnotation(
        tag = context.getString(R.string.tag_privacy),
        annotation = context.getString(R.string.tag_privacy)
    )
    withStyle(style = SpanStyle(fontFamily = FontFamily(Font(commonCompose.font.urbanist_bold)))) {
        append(context.getString(R.string.privacy))
    }
    pop()
    append(context.getString(commonAndroid.string.period))
}

@Preview(showBackground = true)
@Composable
fun PreviewGetStartedScreen() {
    BaseplateTheme {
        GetStartedScreen(
            state = GetStartedScreenViewModel.GetStartedViewState(),
            onClickContinue = {},
            onUserNameChange = {}
        )
    }
}
