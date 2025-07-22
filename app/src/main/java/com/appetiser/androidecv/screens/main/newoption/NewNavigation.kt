package com.appetiser.androidecv.screens.main.newoption

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.appetiser.core.common.compose.result.resultBackNavigator
import kotlinx.serialization.Serializable


@Serializable
data object NewDialog

fun NavGraphBuilder.newDestination(
    navController: NavController
) {
    dialog<NewDialog> {
        val resultBackNavigator = resultBackNavigator<NewDialog, String>(navController)
        NewBottomSheet(
            onBack = {
                if (it != null) {
                    resultBackNavigator.navigateBack(it)
                } else {
                    resultBackNavigator.navigateBack()
                }
            }
        )
    }
}
