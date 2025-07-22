package com.appetiser.feature.permission.provider.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateTextButton
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.permission.provider.R
import com.appetiser.core.common.compose.R as commonR

/**
 * A sample dialog for showing a permission rationale, explaining why your app needs certain permissions.
 * This is for demonstration purposes only; please customize the title, body, and icon to match your app’s design.
 *
 * @param title Title text of the dialog (e.g., "Permission Required").
 * @param body Body text providing context for why the permission is requested.
 * @param icon Optional icon to display, or null if no icon is needed.
 * @param onDismiss Invoked when the user dismisses the dialog (e.g., tapping outside or a cancel button).
 * @param onConfirm Invoked when the user confirms (e.g., proceeds to request permission again).
 */
@Composable
fun PermissionRationaleDialog(
    title: String = stringResource(R.string.permission_dialog_title),
    body: String = stringResource(R.string.permission_dialog_body),
    icon: ImageVector = Icons.Default.Warning,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(body) },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BaseplateTheme.colors.primaryDefault
            )
        },
        confirmButton = {
            BaseplateButton(
                onClick = onConfirm,
                text = stringResource(R.string.proceed)
            )
        },
        dismissButton = {
            BaseplateTextButton(
                onClick = onDismiss,
                text = stringResource(commonR.string.cancel)
            )
        }
    )
}
