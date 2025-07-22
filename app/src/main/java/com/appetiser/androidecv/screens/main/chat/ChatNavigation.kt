package com.appetiser.androidecv.screens.main.chat

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ChatRoute

fun NavGraphBuilder.chatDestination() {
    composable<ChatRoute> {
        ChatScreen()
    }
}
