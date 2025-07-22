package com.appetiser.core.common.compose.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable


/**
 * Displays a snackbar with the provided [BaseplateSnackbarVisuals].
 *
 * This function is a convenience wrapper around [SnackbarHostState.showSnackbar]
 * that specifically uses [BaseplateSnackbarVisuals] for the snackbar's visuals.
 *
 * @param visuals The visuals for the snackbar, encapsulated within a [BaseplateSnackbarVisuals] object.
 */
suspend fun SnackbarHostState.showBaseplateSnackbar(
    visuals: BaseplateSnackbarVisuals
) {
    showSnackbar(visuals = visuals)
}

/**
 * Shows an info [BaseplateSnackbar] with the given message and duration.
 *
 * @param message The message to display in the Snackbar.
 * @param duration The duration for which the Snackbar should be displayed.
 */
suspend fun SnackbarHostState.showBaseplateInfoSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    showBaseplateSnackbar(
        visuals = BaseplateSnackbarVisuals.Info(message = message, duration = duration)
    )
}

/**
 * Shows a success [BaseplateSnackbar] with the given message and duration.
 *
 * @param message The message to display in the Snackbar.
 * @param duration The duration for which the Snackbar should be displayed.
 */
suspend fun SnackbarHostState.showBaseplateSuccessSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    showBaseplateSnackbar(
        visuals = BaseplateSnackbarVisuals.Success(message = message, duration = duration)
    )
}

/**
 * Shows an error [BaseplateSnackbar] with the given message and duration.
 *
 * @param message The message to display in the Snackbar.
 * @param duration The duration for which the Snackbar should be displayed.
 */
suspend fun SnackbarHostState.showBaseplateErrorSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    showBaseplateSnackbar(
        visuals = BaseplateSnackbarVisuals.Error(message = message, duration = duration)
    )
}

/**
 * Displays a custom snackbar with an "info" style, using the provided message, optional icon,
 * optional action, and duration.
 *
 * This function is a convenience wrapper around [showBaseplateSnackbar], specifically creating
 * a [BaseplateSnackbarVisuals.InfoCustom] for the visuals.
 *
 * @param message The message to be displayed in the snackbar.
 * @param icon An optional composable function that renders an icon to be displayed in the snackbar.
 *             Defaults to `null` if no icon is needed.
 * @param action An optional composable function that renders an action button in the snackbar.
 *               Defaults to `null` if no action is needed.
 * @param duration The duration the snackbar will be displayed.
 *                Defaults to [SnackbarDuration.Short].
 */
suspend fun SnackbarHostState.showBaseplateInfoCustomSnackbar(
    message: String,
    icon: @Composable (() -> Unit)? = null,
    action: @Composable ((performAction: () -> Unit) -> Unit)? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    showBaseplateSnackbar(
        visuals = BaseplateSnackbarVisuals.InfoCustom(
            message = message,
            duration = duration,
            icon = icon,
            action = action
        )
    )
}
