package com.appetiser.androidecv.screens.main.notification

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object NotificationRoute

fun NavGraphBuilder.notificationDestination() {
    composable<NotificationRoute> {
        NotificationScreen()
    }
}
