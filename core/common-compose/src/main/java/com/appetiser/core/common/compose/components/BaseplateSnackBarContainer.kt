package com.appetiser.core.common.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost


/**
 * A composable function that wraps content within a scaffold that includes a SnackbarHost for displaying
 * snackbars. This container allows snackbars to appear above the provided content, enabling user-friendly
 * feedback for actions like success, error, info, or confirmation.
 *
 * @param modifier A [Modifier] to customize the layout and behavior of the container.
 * @param snackbarHostState A [SnackbarHostState] used to manage and show snackbar messages.
 * @param content The main content to be displayed inside the container.
 *
 * The snackbar is styled using BaseplateTheme for consistent theming and allows for an action button
 * if an action label is provided. Refer to the design specification in the linked Figma for further
 * enhancements:
 * https://www.figma.com/design/nn2Whd2tNOpcaZDhYMKjDU/Momentum-Baseplate?node-id=24-8286&node-type=frame&t=KXBP4JTOkv4JBUv2-0
 */
@Deprecated(
    message = "Use BaseplateSnackbarHost directly instead. " +
        "This wrapper limits customization and will be removed in future versions."
)
@Composable
fun BaseplateSnackBarContainer(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit
) {
    Scaffold(
        snackbarHost = {
            BaseplateSnackbarHost(snackbarHostState)
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center, // Align content to the center
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            content()
        }
    }
}
