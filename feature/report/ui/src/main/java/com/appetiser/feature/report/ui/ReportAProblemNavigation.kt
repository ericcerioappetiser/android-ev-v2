package com.appetiser.feature.report.ui

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ReportAProblemRoute

fun NavGraphBuilder.reportAProblemDestination(onNavigateBack: () -> Unit = {}) {
    composable<ReportAProblemRoute> {
        ReportAProblemScreen(
            modifier = Modifier,
            onNavigateBack = onNavigateBack,
        )
    }
}
