package com.appetiser.androidecv.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.appetiser.androidecv.screens.main.MainRoute
import com.appetiser.androidecv.screens.main.mainDestination
import com.appetiser.core.common.compose.utils.navigateUpToRoot
import com.appetiser.feature.auth.ui.ForgotPasswordRoute
import com.appetiser.feature.auth.ui.GetStartedRoute
import com.appetiser.feature.auth.ui.LoginViaEmailRoute
import com.appetiser.feature.auth.ui.LoginViaPhoneRoute
import com.appetiser.feature.auth.ui.SignUpViaEmailRoute
import com.appetiser.feature.auth.ui.VerifyOtpRoute
import com.appetiser.feature.auth.ui.forgotPasswordDestination
import com.appetiser.feature.auth.ui.getStartedDestination
import com.appetiser.feature.auth.ui.loginDestination
import com.appetiser.feature.auth.ui.loginPhoneDestination
import com.appetiser.feature.auth.ui.signUpViaEmailDestination
import com.appetiser.feature.auth.ui.verifyOtpDestination
import com.appetiser.feature.song.ui.songDestination
import com.appetiser.feature.walkthrough.ui.WalkthroughRoute
import com.appetiser.feature.walkthrough.ui.walkthroughDestination
import com.appetiser.onboarding.ui.InputBirthdayRoute
import com.appetiser.onboarding.ui.InputNameRoute
import com.appetiser.onboarding.ui.YourProfileRoute
import com.appetiser.onboarding.ui.birthdayDestination
import com.appetiser.onboarding.ui.inputNameDestination
import com.appetiser.onboarding.ui.inputProfileScreen

/**
 * Navigation setup for features follows a shared pattern.
 * Refer to the Guru documentation for how to define and register destinations.
 * @see <a href="https://app.getguru.com/card/cKXgMLdi/Standard-Navigation-for-Feature-Modules">
 *      Standard Navigation for Feature Modules
 *     </a>
 */
@Composable
fun RootNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute
    ) {
        // Project specific

        songDestination(navController)
        mainDestination()

        // Walkthrough module
        walkthroughDestination(
            onDone = {
                navController.navigate(GetStartedRoute) {
                    popUpTo(WalkthroughRoute)
                }
            }
        )

        // Auth module
        getStartedDestination(
            onLoginPhone = {
                navController.navigate(LoginViaPhoneRoute(it))
            },
            onLoginEmail = {
                navController.navigate(LoginViaEmailRoute(it))
            },
            onSignUpEmail = {
                navController.navigate(SignUpViaEmailRoute(it))
            },
            onSignUpPhone = { phoneNumber ->
                navController.navigate(VerifyOtpRoute(username = phoneNumber))
            }
        )

        loginDestination(
            onBackClick = {
                navController.popBackStack()
            },
            toForgotPassword = {
                navController.navigate(
                    ForgotPasswordRoute(
                        email = it
                    )
                )
            },
            onNavigateToHomeScreen = {
                navController.navigateUpToRoot(MainRoute)
            },
            onNavigateToBirthdayScreen = {
                navController.navigate(InputBirthdayRoute)
            },
            onNavigateToProfileScreen = {
                navController.navigate(YourProfileRoute)
            },
            onNavigateToVerifyScreen = {
                navController.navigate(VerifyOtpRoute(it))
            },
            onNavigateToAddNameScreen = {
                navController.navigate(InputNameRoute)
            }
        )

        forgotPasswordDestination(
            toPreviousScreen = {
                navController.navigateUp()
            }
        )

        inputNameDestination(
            onNameInputDone = {
                navController.navigate(InputBirthdayRoute)
            },
        )

        birthdayDestination(
            onBirthdayInputDone = {
                navController.navigate(YourProfileRoute)
            },
            onShouldExitAppOnBack = {
                navController.navigate(MainRoute) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            onNavigateUp = {
                navController.navigateUp()
            }
        )

        inputProfileScreen(
            onProfileInputDone = {
                navController.navigate(MainRoute) {
                    popUpTo(InputNameRoute) {
                        inclusive = true
                    }
                }
            },
            onBackClick = { navController.navigateUp() },
            onNavigateToHome = {
                navController.navigate(MainRoute) {
                    popUpTo(InputNameRoute) {
                        inclusive = true
                    }
                }
            },
        )

        loginPhoneDestination(
            onBackClick = { navController.popBackStack() },
            onLoginPhoneSuccess = { navController.navigateUpToRoot(MainRoute) }
        )

        signUpViaEmailDestination(
            onBackClick = { navController.popBackStack() },
            onCreatePasswordDone = {
                navController.navigate(VerifyOtpRoute(it)) {
                    popUpTo(SignUpViaEmailRoute(email = it)) {
                        inclusive = true
                    }
                }
            }
        )

        verifyOtpDestination(
            onOtpVerified = {
                navController.navigate(InputNameRoute) {
                    popUpTo<VerifyOtpRoute> {
                        inclusive = true
                    }
                }
            },
            onUserLoggedOut = {
                navController.navigate(GetStartedRoute) {
                    popUpTo<VerifyOtpRoute> {
                        inclusive = true
                    }
                }
            }
        )
    }
}
