package com.appetiser.feature.profile.ui.accountsettings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.appetiser.core.domain.user.UsernameType
import com.appetiser.feature.auth.ui.ForgotPasswordRoute
import com.appetiser.feature.auth.ui.forgotPasswordDestination
import com.appetiser.feature.profile.ui.accountsettings.deleteaccount.DeleteAccountScreen
import com.appetiser.feature.profile.ui.accountsettings.newemail.NewEmailScreen
import com.appetiser.feature.profile.ui.accountsettings.newpassword.NewPasswordScreen
import com.appetiser.feature.profile.ui.accountsettings.newphone.NewPhoneScreen
import kotlinx.serialization.Serializable

@Serializable
data object AccountSettingsGraph

fun NavGraphBuilder.accountSettingsGraph(
    navController: NavController
) {
    navigation<AccountSettingsGraph>(
        startDestination = AccountSettingsRoute
    ) {
        accountSettingsScreen(
            toPreviousScreen = {
                navController.navigateUp()
            },
            toNewEmail = {
                navController.navigate(NewEmailRoute)
            },
            toNewPhone = {
                navController.navigate(NewPhoneRoute)
            },
            toNewPassword = {
                navController.navigate(NewPasswordRoute)
            },
            toDeleteAccount = {
                navController.navigate(DeleteAccountRoute)
            }
        )
        newEmailScreen(
            toPreviousScreen = {
                navController.navigateUp()
            },
            toForgotPassword = {
                navController.navigate(
                    ForgotPasswordRoute(
                        email = it
                    )
                )
            }
        )
        newPhoneScreen {
            navController.navigateUp()
        }
        newPasswordScreen(
            toPreviousScreen = {
                navController.navigateUp()
            },
            toForgotPassword = {
                navController.navigate(
                    ForgotPasswordRoute(
                        email = it
                    )
                )
            }
        )
        forgotPasswordDestination(
            toPreviousScreen = {
                navController.navigateUp()
            }
        )
        deleteAccountScreen(
            toPreviousScreen = {
                navController.navigateUp()
            },
            toForgotPassword = {
                navController.navigate(
                    ForgotPasswordRoute(
                        email = it
                    )
                )
            }
        )
    }
}

@Serializable
data object AccountSettingsRoute

fun NavGraphBuilder.accountSettingsScreen(
    toPreviousScreen: () -> Unit,
    toNewEmail: () -> Unit,
    toNewPhone: () -> Unit,
    toNewPassword: () -> Unit,
    toDeleteAccount: () -> Unit
) {
    composable<AccountSettingsRoute> {
        AccountSettingsScreen(
            toPreviousScreen = toPreviousScreen,
            toNewUsername = {
                when (it) {
                    UsernameType.EMAIL -> toNewEmail()
                    UsernameType.PHONE_NUMBER -> toNewPhone()
                    else -> {}
                }
            },
            toNewPassword = toNewPassword,
            toDeleteAccount = toDeleteAccount
        )
    }
}

@Serializable
data object NewEmailRoute

fun NavGraphBuilder.newEmailScreen(
    toPreviousScreen: () -> Unit,
    toForgotPassword: (String) -> Unit
) {
    composable<NewEmailRoute> {
        NewEmailScreen(
            toPreviousScreen = toPreviousScreen,
            toForgotPassword = toForgotPassword
        )
    }
}

@Serializable
data object NewPhoneRoute

fun NavGraphBuilder.newPhoneScreen(
    toPreviousScreen: () -> Unit
) {
    composable<NewPhoneRoute> {
        NewPhoneScreen(toPreviousScreen = toPreviousScreen)
    }
}

@Serializable
data object NewPasswordRoute

fun NavGraphBuilder.newPasswordScreen(
    toPreviousScreen: () -> Unit,
    toForgotPassword: (String) -> Unit
) {
    composable<NewPasswordRoute> {
        NewPasswordScreen(
            toPreviousScreen = toPreviousScreen,
            toForgotPassword = toForgotPassword
        )
    }
}

@Serializable
data object DeleteAccountRoute

fun NavGraphBuilder.deleteAccountScreen(
    toPreviousScreen: () -> Unit,
    toForgotPassword: (String) -> Unit
) {
    composable<DeleteAccountRoute> {
        DeleteAccountScreen(
            toPreviousScreen = toPreviousScreen,
            toForgotPassword = toForgotPassword
        )
    }
}
