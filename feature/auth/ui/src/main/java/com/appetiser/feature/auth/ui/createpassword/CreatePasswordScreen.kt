package com.appetiser.feature.auth.ui.createpassword

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.android.util.PasswordRulesUtil
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateCircularIndicator
import com.appetiser.core.common.compose.components.BaseplatePasswordRuleItem
import com.appetiser.core.common.compose.components.BaseplatePasswordTextField
import com.appetiser.core.common.compose.components.BaseplateSnackBarContainer
import com.appetiser.core.common.compose.components.CenterTopBar
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.auth.ui.R

@Composable
fun CreatePasswordScreen(
    viewModel: CreatePasswordViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCreatePasswordDone: (String) -> Unit = {}
) {
    val state by viewModel.collectAsState()

    CreatePasswordScreen(
        state = state,
        onPasswordChange = {
            viewModel.action(CreatePasswordViewModel.InputAction.InputPassword(it))
        },
        onClickContinue = {
            viewModel.action(CreatePasswordViewModel.InputAction.ClickContinue)
        },
        onBackClick = onBackClick,
        onCreatePasswordDone = onCreatePasswordDone
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun CreatePasswordScreen(
    state: CreatePasswordViewModel.CreatePasswordViewState,
    onPasswordChange: (String) -> Unit = { },
    onClickContinue: () -> Unit = { },
    onBackClick: () -> Unit = { },
    onCreatePasswordDone: (String) -> Unit = { }
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val passwordRules = remember(context) { PasswordRulesUtil.getPasswordRules(context) }

    ConsumableSideEffect(state.shouldNavigateToNextScreen) {
        onCreatePasswordDone(it)
    }

    ConsumableSideEffect(state.error) {
        snackbarHostState.showSnackbar(
            message = it.getThrowableError(),
            actionLabel = context.getString(R.string.hide)
        )
    }

    BaseplateSnackBarContainer(snackbarHostState = snackbarHostState) {
        if (state.isLoading) {
            BaseplateCircularIndicator()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {
            CenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = "",
                navigationIcon = {
                    IconButton(onClick = { onBackClick.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = BaseplateTheme.colors.textTertiary
                        )
                    }
                },
                actions = {
                },
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Paddings.LARGE),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.create_a_password),
                        style = BaseplateTheme.typography.title1
                    )
                    Spacer(modifier = Modifier.height(Paddings.SMALL))
                    Text(
                        text = stringResource(R.string.create_password_desc),
                        style = BaseplateTheme.typography.bodyRegular,
                        color = BaseplateTheme.colors.textSecondary
                    )
                    Text(
                        text = state.email,
                        style = BaseplateTheme.typography.bodyBold
                    )
                    Spacer(modifier = Modifier.height(Paddings.X_LARGE))

                    BaseplatePasswordTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(id = R.string.password),
                                style = BaseplateTheme.typography.bodyRegular
                            )
                        },
                        password = state.password,
                        onPasswordChange = onPasswordChange,
                        trailingIconOff = ImageVector.vectorResource(R.drawable.ic_hide_password),
                        trailingIconOn = ImageVector.vectorResource(R.drawable.ic_show_password),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(Paddings.LARGE))
                    Text(
                        text = stringResource(R.string.password_must_contain),
                        style = BaseplateTheme.typography.captionBold,
                        color = BaseplateTheme.colors.textTertiary
                    )
                    Spacer(modifier = Modifier.height(Paddings.LARGE))

                    passwordRules.forEach { (ruleText, validator) ->
                        BaseplatePasswordRuleItem(
                            ruleText to validator,
                            password = state.password,
                            iconContent = { isValid ->
                                Icon(
                                    ImageVector.vectorResource(
                                        if (isValid) R.drawable.ic_check else R.drawable.ic_button
                                    ),
                                    contentDescription = null,
                                    tint = if (isValid) BaseplateTheme.colors.semanticSuccess else BaseplateTheme.colors.iconSecondary,
                                )
                            },
                            textContent = { isValid, rule ->
                                Text(
                                    text = rule,
                                    color = if (isValid) BaseplateTheme.colors.semanticSuccess else BaseplateTheme.colors.textSecondary,
                                    style = BaseplateTheme.typography.footnoteRegular
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                BaseplateButton(
                    shape = RoundedCornerShape(Paddings.MEDIUM),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = passwordRules.all { it.second(state.password) },
                    onClick = onClickContinue,
                    text = stringResource(id = R.string.btn_continue),
                )
            }
        }
    }
}

@Preview
@Composable
fun CreatePasswordScreenPreview() {
    BaseplateTheme {
        CreatePasswordScreen(
            state = CreatePasswordViewModel.CreatePasswordViewState()
        )
    }
}


