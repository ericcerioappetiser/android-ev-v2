package com.appetiser.core.common.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.appetiser.core.common.compose.dimensions.Paddings


@Composable
fun SnackbarContent(snackbarData: SnackbarData) {
    Snackbar(
        modifier = Modifier.padding(Paddings.LARGE),
        containerColor = if (snackbarData.visuals.actionLabel.isNullOrBlank()) {
            MaterialTheme.colorScheme.surfaceBright
        } else {
            MaterialTheme.colorScheme.error
        },
        contentColor = if (snackbarData.visuals.actionLabel.isNullOrBlank()) {
            MaterialTheme.colorScheme.surfaceContainerLow
        } else {
            MaterialTheme.colorScheme.onSecondary
        },
        actionContentColor = if (snackbarData.visuals.actionLabel.isNullOrBlank()) {
            MaterialTheme.colorScheme.surfaceContainerLow
        } else {
            MaterialTheme.colorScheme.onSecondary
        },
        action = {
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { snackbarData.performAction() }) {
                    Text(actionLabel)
                }
            }
        }
    ) {
        Text(snackbarData.visuals.message)
    }
}
