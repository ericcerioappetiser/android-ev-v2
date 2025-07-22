package com.appetiser.core.common.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.ui.BaseplateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseplateBottomSheetOptions(
    scaffoldState: BottomSheetScaffoldState,
    sheetContent: @Composable ColumnScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable (BottomSheetScaffoldState) -> Unit
) {
    BottomSheetScaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars),
        sheetContainerColor = BaseplateTheme.colors.surfaceBackground,
        sheetContentColor = BaseplateTheme.colors.primaryDefault,
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars) // Apply padding inside sheet content
                    .consumeWindowInsets(WindowInsets.navigationBars) // Consume insets to prevent over-padding
            ) {
                sheetContent()
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        content(scaffoldState)
    }
}

@Composable
fun BottomSheetOption(
    text: String,
    onClick: () -> Unit
) {
    BaseplateTextButton(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        onClick = onClick
    )
}

data class BottomSheetItem(val text: String, val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = false)
@Composable
fun BottomSheetPreview() {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded, skipHiddenState = false
        )
    )

    val options = listOf(
        BottomSheetItem(
            text = stringResource(R.string.ok),
            onClick = {}
        ),

        BottomSheetItem(
            text = stringResource(R.string.cancel),
            onClick = {}
        )
    )

    val sheetContent: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BaseplateTheme.colors.surfaceBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            options
                .forEachIndexed { index, option ->
                    BottomSheetOption(
                        text = option.text,
                        onClick = option.onClick
                    )
                    if (index != options.lastIndex) {
                        HorizontalDivider(
                            color = Color.LightGray,
                            thickness = 1.dp
                        )
                    }
                }
        }
    }

    BaseplateBottomSheetOptions(
        scaffoldState,
        sheetContent = sheetContent,
        content = {},
    )
}
