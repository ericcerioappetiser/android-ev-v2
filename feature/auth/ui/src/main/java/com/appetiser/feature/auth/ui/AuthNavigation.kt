package com.appetiser.feature.auth.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.appetiser.feature.auth.ui.createpassword.CreatePasswordScreen
import com.appetiser.feature.auth.ui.forgotpassword.ForgotPasswordScreen
import com.appetiser.feature.auth.ui.getstarted.GetStartedScreen
import com.appetiser.feature.auth.ui.login.LoginScreenRoot
import com.appetiser.feature.auth.ui.login.phone.LoginPhoneScreen
import kotlinx.serialization.Serializable

// TODO: Each of the route and destination should be in their each on file name [Feature]Navigation.kt

@Serializable
data object GetStartedRoute

@Serializable
data class LoginViaEmailRoute(val email: String)

@Serializable
data class ForgotPasswordRoute(val email: String)

@Serializable
data class LoginViaPhoneRoute(val phoneNumber: String)

@Serializable
data class SignUpViaEmailRoute(val email: String)

@Serializable
data class VerifyOtpRoute(val username: String)

// region Screens

fun NavGraphBuilder.getStartedDestination(
    onLoginPhone: (String) -> Unit,
    onLoginEmail: (String) -> Unit,
    onSignUpEmail: (String) -> Unit,
    onSignUpPhone: (String) -> Unit
) {
    composable<GetStartedRoute> {
        GetStartedScreen(
            onLoginPhone = onLoginPhone,
            onLoginEmail = onLoginEmail,
            onSignUpEmail = onSignUpEmail,
            onSignUpPhone = onSignUpPhone
        )
    }
}

fun NavGraphBuilder.loginDestination(
    onBackClick: () -> Unit,
    toForgotPassword: (String) -> Unit,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToVerifyScreen: (String) -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToBirthdayScreen: () -> Unit,
    onNavigateToAddNameScreen: () -> Unit
) {
    composable<LoginViaEmailRoute> {
        LoginScreenRoot(
            onBackClick = onBackClick,
            toForgotPassword = toForgotPassword,
            onNavigateToHomeScreen = onNavigateToHomeScreen,
            onNavigateToVerifyScreen = onNavigateToVerifyScreen,
            onNavigateToProfileScreen = onNavigateToProfileScreen,
            onNavigateToBirthdayScreen = onNavigateToBirthdayScreen,
            onNavigateToAddNameScreen = onNavigateToAddNameScreen
        )
    }
}

fun NavGraphBuilder.forgotPasswordDestination(
    toPreviousScreen: () -> Unit
) {
    composable<ForgotPasswordRoute> {
        ForgotPasswordScreen(toPreviousScreen = toPreviousScreen)
    }
}

fun NavGraphBuilder.loginPhoneDestination(
    onBackClick: () -> Unit,
    onLoginPhoneSuccess: () -> Unit
) {
    composable<LoginViaPhoneRoute> {
        LoginPhoneScreen(
            onLoginPhoneSuccess = onLoginPhoneSuccess,
            onBackClick = onBackClick
        )
    }
}

fun NavGraphBuilder.signUpViaEmailDestination(
    onBackClick: () -> Unit,
    onCreatePasswordDone: (String) -> Unit
) {
    composable<SignUpViaEmailRoute> {
        // TODO: Rename, not up to standard with other screens naming
        CreatePasswordScreen(
            onBackClick = onBackClick,
            onCreatePasswordDone = onCreatePasswordDone
        )
    }
}

fun NavGraphBuilder.verifyOtpDestination(
    onOtpVerified: (String) -> Unit,
    onUserLoggedOut: () -> Unit
) {
    composable<VerifyOtpRoute> {
        VerifyOtpScreenRoot(
            onOtpVerified = onOtpVerified,
            onUserLoggedOut = onUserLoggedOut

        )
    }
}

// endregion
