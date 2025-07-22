package com.appetiser.core.common.compose.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable

sealed class BaseplateSnackbarVisuals : SnackbarVisuals {

    // Baseplate theming specs, dont have action label
    override val actionLabel: String? = null

    // Baseplate theming specs, dont have dismiss action
    override val withDismissAction: Boolean = false

    data class Info(
        override val message: String,
        override val duration: SnackbarDuration
    ) : BaseplateSnackbarVisuals()

    data class Success(
        override val message: String,
        override val duration: SnackbarDuration
    ) : BaseplateSnackbarVisuals()

    data class Error(
        override val message: String,
        override val duration: SnackbarDuration
    ) : BaseplateSnackbarVisuals()

    data class InfoCustom(
        override val message: String,
        override val duration: SnackbarDuration,
        val icon: @Composable (() -> Unit)? = null,
        val action: @Composable ((performAction: () -> Unit) -> Unit)? = null
    ) : BaseplateSnackbarVisuals()
}
