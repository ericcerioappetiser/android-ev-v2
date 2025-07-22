package com.appetiser.core.common.compose.utils

import androidx.navigation.NavController

fun <T : Any> NavController.navigateUpToRoot(route: T) {
    navigate(route) {
        popUpTo(graph.id) // pop up to the root, which is our nav graph
    }
}
