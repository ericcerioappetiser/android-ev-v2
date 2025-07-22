package com.appetiser.feature.profile.ui.accountsettings

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.components.BaseplateTextButton
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.domain.user.UsernameType
import com.appetiser.feature.profile.ui.R
import com.appetiser.feature.profile.ui.editprofile.EditProfileBar
import com.appetiser.core.common.compose.R as commonComposeR

@Composable
fun AccountSettingsScreen(
    toPreviousScreen: () -> Unit,
    toNewUsername: (UsernameType) -> Unit,
    toNewPassword: () -> Unit,
    toDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<AccountSettingsViewModel>()
    val viewState by viewModel.collectAsState()

    AccountSettingsScreen(
        viewState = viewState,
        toPreviousScreen = toPreviousScreen,
        toNewUsername = toNewUsername,
        toNewPassword = toNewPassword,
        onAccountLogout = {
            viewModel.action(AccountSettingsViewModel.ActionState.LogoutCurrentUser)
        },
        toDeleteAccount = toDeleteAccount,
        modifier = modifier
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun AccountSettingsScreen(
    viewState: AccountSettingsViewModel.ViewState,
    toPreviousScreen: () -> Unit,
    toNewUsername: (UsernameType) -> Unit,
    toNewPassword: () -> Unit,
    onAccountLogout: () -> Unit,
    toDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EditProfileBar(
                title = stringResource(commonComposeR.string.account_settings),
                toPreviousScreen = toPreviousScreen
            )
        }
    ) { padding ->
        AccountSettingsContent(
            usernameType = viewState.usernameType,
            username = viewState.username,
            toNewUsername = toNewUsername,
            toNewPassword = toNewPassword,
            onAccountLogout = onAccountLogout,
            toDeleteAccount = toDeleteAccount,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

@Composable
fun AccountSettingsContent(
    usernameType: UsernameType,
    username: String,
    toNewUsername: (UsernameType) -> Unit,
    toNewPassword: () -> Unit,
    onAccountLogout: () -> Unit,
    toDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        when (usernameType) {
            UsernameType.EMAIL -> {
                AccountSettingItem(
                    value = username,
                    contentDescription = stringResource(R.string.change_email_address),
                    modifier = Modifier.clickable {
                        toNewUsername(usernameType)
                    },
                    label = stringResource(R.string.email_address)
                )
                AccountSettingItem(
                    value = stringResource(commonComposeR.string.password),
                    contentDescription = stringResource(R.string.change_password),
                    modifier = Modifier.clickable(onClick = toNewPassword)
                )
            }
            UsernameType.PHONE_NUMBER -> {
                AccountSettingItem(
                    value = username,
                    contentDescription = stringResource(R.string.change_phone_number),
                    modifier = Modifier.clickable {
                        toNewUsername(usernameType)
                    },
                    label = stringResource(R.string.phone_number)
                )
            }
            else -> {}
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = Paddings.LARGE, vertical = Paddings.X_LARGE)
                .height(1.dp),
            color = BaseplateTheme.colors.borderDefault
        )
        LogoutAccountSetting(onAccountLogout = onAccountLogout)
        DeleteAccountSetting(toDeleteAccount = toDeleteAccount)
    }
}

@Composable
fun AccountSettingItem(
    value: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    valueColor: Color = BaseplateTheme.colors.textPrimary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Paddings.LARGE),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1.0F)
                .padding(vertical = Paddings.LARGE)
        ) {
            label?.let {
                Text(
                    text = label,
                    color = BaseplateTheme.colors.textSecondary,
                    style = BaseplateTheme.typography.footnoteRegular
                )
            }
            Text(
                text = value,
                color = valueColor,
                style = BaseplateTheme.typography.bodyRegular
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(commonComposeR.drawable.ic_chevron_right),
            contentDescription = contentDescription
        )
    }
}

@Composable
fun LogoutAccountSetting(
    onAccountLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmation by remember { mutableStateOf(false) }

    AccountSettingItem(
        value = stringResource(R.string.log_out),
        contentDescription = stringResource(R.string.log_out_your_account),
        modifier = modifier.clickable {
            showConfirmation = true
        }
    )

    if (showConfirmation) {
        AccountSettingsDialog(
            title = stringResource(R.string.log_out),
            description = stringResource(R.string.are_you_sure_you_want),
            onConfirmationDismiss = {
                showConfirmation = false
            },
            positiveText = stringResource(R.string.log_out),
            onPositiveClick = {
                onAccountLogout()
                showConfirmation = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsDialog(
    title: String,
    description: String,
    onConfirmationDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    positiveText: String? = null,
    onPositiveClick: () -> Unit = {}
) {
    BasicAlertDialog(
        onDismissRequest = onConfirmationDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(BaseplateTheme.colors.surfaceBackground)
                .padding(Paddings.X_LARGE),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = BaseplateTheme.colors.textPrimary,
                style = BaseplateTheme.typography.title3
            )
            Spacer(modifier = Modifier.height(Paddings.LARGE))
            Text(
                text = description,
                modifier = Modifier.fillMaxWidth(),
                color = BaseplateTheme.colors.textPrimary,
                style = BaseplateTheme.typography.bodyRegular
            )
            Spacer(modifier = Modifier.height(Paddings.X_LARGE))
            Row {
                positiveText?.let {
                    BaseplateTextButton(onClick = onPositiveClick) {
                        Text(
                            text = it,
                            style = BaseplateTheme.typography.bodyBold
                        )
                    }
                }
                BaseplateTextButton(onClick = onConfirmationDismiss) {
                    Text(
                        text = stringResource(commonComposeR.string.cancel),
                        style = BaseplateTheme.typography.bodyBold
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteAccountSetting(
    toDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmation by remember { mutableStateOf(false) }

    AccountSettingItem(
        value = stringResource(R.string.delete_account),
        contentDescription = stringResource(R.string.delete_your_account),
        modifier = modifier.clickable {
            showConfirmation = true
        },
        valueColor = BaseplateTheme.colors.semanticErrorDefault
    )

    if (showConfirmation) {
        AccountSettingsDialog(
            title = stringResource(R.string.delete_account),
            description = stringResource(R.string.are_you_sure_you_want_1),
            onConfirmationDismiss = {
                showConfirmation = false
            },
            positiveText = stringResource(R.string.continue_string),
            onPositiveClick = toDeleteAccount
        )
    }
}

@Preview
@Composable
private fun AccountSettingsScreenPreview() {
    BaseplateTheme {
        AccountSettingsScreen(
            viewState = AccountSettingsViewModel.ViewState(
                usernameType = UsernameType.EMAIL
            ),
            toPreviousScreen = {},
            toNewUsername = {},
            toNewPassword = {},
            onAccountLogout = {},
            toDeleteAccount = {}
        )
    }
}
