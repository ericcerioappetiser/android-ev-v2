package com.appetiser.core.common.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun BaseplateCustomInputScreen(
    modifier: Modifier = Modifier,
    background: Color = BaseplateTheme.colors.surfaceBackground,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    headerContent: @Composable () -> Unit,
    topBarContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
    footerContent: @Composable () -> Unit,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    Snackbar(
                        modifier = Modifier.padding(Paddings.LARGE),
                        containerColor = BaseplateTheme.colors.semanticErrorDefault,
                        contentColor = BaseplateTheme.colors.textInverted,
                        actionContentColor = BaseplateTheme.colors.textInverted,
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
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(background)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                topBarContent()


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Paddings.LARGE)

                ) {
                    headerContent()
                    content()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding()
            ) {
                footerContent()
            }
        }
    }
}
