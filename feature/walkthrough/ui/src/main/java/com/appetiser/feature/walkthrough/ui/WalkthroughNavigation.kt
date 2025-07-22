package com.appetiser.feature.walkthrough.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object WalkthroughRoute

// region Screens

fun NavGraphBuilder.walkthroughDestination(
    onDone: () -> Unit = {}
) {
    composable<WalkthroughRoute> {
        WalkthroughScreen(
            onDone = onDone
        )
    }
}

// endregion
