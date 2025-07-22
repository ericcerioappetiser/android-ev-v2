package com.appetiser.onboarding.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.appetiser.onboarding.ui.inputname.InputNameScreen
import com.appetiser.onboarding.ui.profile.YourProfileScreenRoot
import com.appetiser.onboarding.ui.yourbirthday.YourBirthdayScreen
import kotlinx.serialization.Serializable


// region Screens
@Serializable
data object InputNameRoute

@Serializable
data object InputBirthdayRoute

@Serializable
data object YourProfileRoute

fun NavGraphBuilder.inputNameDestination(
    onNameInputDone: () -> Unit,
) {
    composable<InputNameRoute> {
        InputNameScreen(
            onNameInputDone = onNameInputDone,
        )
    }
}

fun NavGraphBuilder.birthdayDestination(
    onBirthdayInputDone: () -> Unit,
    onShouldExitAppOnBack: () -> Unit,
    onNavigateUp: () -> Unit
) {
    composable<InputBirthdayRoute> {
        YourBirthdayScreen(
            onBirthdayInputDone = onBirthdayInputDone,
            onShouldExitAppOnBack = onShouldExitAppOnBack,
            onNavigateUp = onNavigateUp
        )
    }
}

fun NavGraphBuilder.inputProfileScreen(
    onProfileInputDone: () -> Unit,
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    composable<YourProfileRoute> {
        YourProfileScreenRoot(
            onProfileInputDone = onProfileInputDone,
            onNavigateUp = onBackClick,
            onNavigateToHome = onNavigateToHome,
        )
    }
}

// endregion
