package com.appetiser.feature.profile.ui

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.appetiser.feature.profile.domain.WebDocument
import com.appetiser.feature.profile.ui.about.AboutScreen
import com.appetiser.feature.profile.ui.about.privacypolicy.PrivacyPolicyScreen
import com.appetiser.feature.profile.ui.accountsettings.AccountSettingsGraph
import com.appetiser.feature.profile.ui.accountsettings.accountSettingsGraph
import com.appetiser.feature.profile.ui.editprofile.EditProfileScreen
import com.appetiser.feature.profile.ui.helpcenter.HelpCenterScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProfileGraph

fun NavGraphBuilder.profileGraph(
    navController: NavController
) {
    navigation<ProfileGraph>(
        startDestination = ProfileRoute
    ) {
        profileScreen(
            toEditProfile = {
                navController.navigate(EditProfileRoute)
            },
            toAccountSettings = {
                navController.navigate(AccountSettingsGraph)
            },
            toAbout = {
                navController.navigate(AboutRoute)
            },
            toHelpCentre = {
                navController.navigate(HelpCentreRoute)
            }
        )
        editProfileScreen {
            navController.navigateUp()
        }
        accountSettingsGraph(navController = navController)
        aboutScreen(
            toPreviousScreen = {
                navController.navigateUp()
            },
            toPrivacyPolicy = { document ->
                navController.navigate(
                    PrivacyPolicyRoute(document)
                )
            }
        )
        privacyPolicyScreen {
            navController.navigateUp()
        }
    }
}

@Serializable
data object ProfileRoute

fun NavGraphBuilder.profileScreen(
    toEditProfile: () -> Unit,
    toAccountSettings: () -> Unit,
    toAbout: () -> Unit,
    toHelpCentre: () -> Unit
) {
    composable<ProfileRoute> {
        ProfileScreen(
            toEditProfile = toEditProfile,
            toAccountSettings = toAccountSettings,
            toAbout = toAbout,
            toHelpCentre = toHelpCentre
        )
    }
}

@Serializable
data object EditProfileRoute

fun NavGraphBuilder.editProfileScreen(
    toPreviousScreen: () -> Unit
) {
    composable<EditProfileRoute> {
        EditProfileScreen(toPreviousScreen = toPreviousScreen)
    }
}

@Serializable
data object AboutRoute

fun NavGraphBuilder.aboutScreen(
    toPreviousScreen: () -> Unit,
    toPrivacyPolicy: (WebDocument) -> Unit,
) {
    composable<AboutRoute> {
        AboutScreen(
            toPreviousScreen = toPreviousScreen,
            toPrivacyPolicy = toPrivacyPolicy
        )
    }
}

@Serializable
data class PrivacyPolicyRoute(
    val document: WebDocument
)

fun NavGraphBuilder.privacyPolicyScreen(
    toPreviousScreen: () -> Unit
) {
    composable<PrivacyPolicyRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<PrivacyPolicyRoute>()
        PrivacyPolicyScreen(
            document = route.document,
            toPreviousScreen = toPreviousScreen
        )
    }
}


@Serializable
data object HelpCentreRoute

fun NavGraphBuilder.helpCentreDestination(onBackClick: () -> Unit = {}, onNavigateToReport: () -> Unit = {}) {
    composable<HelpCentreRoute> {
        HelpCenterScreen(
            Modifier,
            onBackClick = onBackClick,
            onNavigateToReport = onNavigateToReport
        )
    }
}

