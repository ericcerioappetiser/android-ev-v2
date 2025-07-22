package com.appetiser.core.common.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun CenterTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    actions: @Composable (() -> Unit)? = null,
    navigationIcon: @Composable (() -> Unit)? = null
) {
    CenterTopBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.padding(vertical = Paddings.LARGE, horizontal = Paddings.LARGE),
                text = title.orEmpty(),
                textAlign = TextAlign.Center,
                style = BaseplateTheme.typography.bodySemiBold
            )
        },
        actions = actions,
        navigationIcon = navigationIcon
    )
}

@Composable
fun CenterTopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable (() -> Unit)? = null,
    navigationIcon: @Composable (() -> Unit)? = null
) {
    Layout(
        modifier = modifier,
        content = {
            navigationIcon?.let {
                Box(
                    modifier = Modifier.layoutId(CenterTopBarSlot.NavigationIcon)
                ) { it() }
            }
            actions?.let {
                Box(
                    modifier = Modifier.layoutId(CenterTopBarSlot.Actions)
                ) { it() }
            }
            Box(
                modifier = Modifier.layoutId(CenterTopBarSlot.Title)
            ) {
                title()
            }
        }

    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val maxWidth = constraints.maxWidth

        val navigationIconPlaceable =
            measurables.firstOrNull { it.layoutId == CenterTopBarSlot.NavigationIcon }
                ?.measure(looseConstraints)

        val navigationIconWidth = navigationIconPlaceable?.width ?: 0
        val navigationIconHeight = navigationIconPlaceable?.height ?: 0

        val actionsMeasurable =
            measurables.firstOrNull { it.layoutId == CenterTopBarSlot.Actions }
                ?.measure(looseConstraints)

        val actionsWidth = actionsMeasurable?.width ?: 0
        val actionsHeight = actionsMeasurable?.height ?: 0

        val actionsMaxWidth = maxOf(navigationIconWidth, actionsWidth)

        val availableTextWidth = (maxWidth - (actionsMaxWidth * 2)).coerceAtLeast(0)

        val titleConstraints = looseConstraints.copy(maxWidth = availableTextWidth)

        val titleMeasurable = if (!titleConstraints.isZero) {
            measurables.firstOrNull { it.layoutId == CenterTopBarSlot.Title }
                ?.measure(titleConstraints)
        } else {
            null
        }

        val titleWidth = titleMeasurable?.width ?: 0
        val titleHeight = titleMeasurable?.height ?: 0

        val totalHeight = maxOf(navigationIconHeight, actionsHeight, titleHeight)
            .coerceAtMost(constraints.maxHeight)

        layout(maxWidth, totalHeight) {
            navigationIconPlaceable?.let {
                it.placeRelative(0, (totalHeight - it.height) / 2)
            }
            titleMeasurable?.let {
                // Center the title
                val offsetX = (maxWidth - titleWidth) / 2
                it.placeRelative(offsetX, (totalHeight - it.height) / 2)
            }
            actionsMeasurable?.let {
                it.placeRelative(maxWidth - it.width, (totalHeight - it.height) / 2)
            }
        }
    }
}

private enum class CenterTopBarSlot {
    NavigationIcon, Title, Actions
}

@Preview(showBackground = true)
@Composable
private fun CenterTopBarPreview() {
    BaseplateTheme {
        CenterTopBar(
            modifier = Modifier,
            title = "Title",
            actions = {
                Text(text = "Action")
            }
        )
    }
}
