package com.appetiser.core.common.compose.components.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A custom SnackbarHost composable that uses the BaseplateSnackbar.
 *
 * This composable provides a standardized way to display snackbars within the application.
 * It leverages the Material 3 [SnackbarHost] and displays either a [BaseplateSnackbar] or a default [Snackbar] based on the visuals type provided in the [SnackbarData].
 *
 * Specifically, if the [SnackbarData.visuals] is an instance of [BaseplateSnackbarVisuals], it uses [BaseplateSnackbar] for its content.
 * Otherwise, it falls back to the standard Material 3 [Snackbar] for its content.
 *
 * @param hostState The [SnackbarHostState] that controls the display and dismissal of snackbars.
 * @param modifier Modifier for the SnackbarHost.
 */
@Composable
fun BaseplateSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        if (data.visuals is BaseplateSnackbarVisuals) {
            BaseplateSnackbar(data)
        } else {
            Snackbar(data)
        }
    }
}
