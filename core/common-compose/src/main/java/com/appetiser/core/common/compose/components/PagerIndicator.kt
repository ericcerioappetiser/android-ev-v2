package com.appetiser.core.common.compose.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.ui.BaseplateTheme
import kotlin.math.roundToInt
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = BaseplateTheme.colors.primaryDefault,
    inactiveColor: Color = BaseplateTheme.colors.borderDefault,
    inactiveIndicatorWidth: Dp = 8.dp,
    inactiveIndicatorHeight: Dp = inactiveIndicatorWidth,
    activeIndicatorWidth: Dp = inactiveIndicatorWidth * 2,
    activeIndicatorHeight: Dp = inactiveIndicatorHeight,
    spacing: Dp = inactiveIndicatorWidth / 2,
    inactiveIndicatorShape: Shape = CircleShape,
    activeIndicatorShape: Shape = inactiveIndicatorShape,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
) {
    val stateBridge = remember(pagerState) {
        object : PagerStateBridge {
            override val pageCount: Int get() = pagerState.pageCount
            override val currentPage: Int get() = pagerState.currentPage
            override val currentPageOffsetFraction: Float get() = pagerState.currentPageOffsetFraction
        }
    }


    PagerIndicator(
        state = stateBridge,
        modifier = modifier,
        spacing = spacing,
        verticalAlignment = verticalAlignment,
        activeIndicator = {
            Box(
                modifier = Modifier
                    .size(width = activeIndicatorWidth, height = activeIndicatorHeight)
                    .background(
                        color = activeColor,
                        shape = activeIndicatorShape
                    )
            )
        },
        inactiveIndicator = {
            Box(
                modifier = Modifier
                    .size(width = inactiveIndicatorWidth, height = inactiveIndicatorHeight)
                    .background(
                        color = inactiveColor,
                        shape = inactiveIndicatorShape
                    )
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeIndicator: @Composable () -> Unit,
    inactiveIndicator: @Composable () -> Unit,
    spacing: Dp = 8.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
) {
    val stateBridge = remember(pagerState) {
        object : PagerStateBridge {
            override val pageCount: Int get() = pagerState.pageCount
            override val currentPage: Int get() = pagerState.currentPage
            override val currentPageOffsetFraction: Float get() = pagerState.currentPageOffsetFraction
        }
    }
    PagerIndicator(
        state = stateBridge,
        modifier = modifier,
        activeIndicator = activeIndicator,
        inactiveIndicator = inactiveIndicator,
        spacing = spacing,
        verticalAlignment = verticalAlignment
    )
}

@Composable
private fun PagerIndicator(
    state: PagerStateBridge,
    modifier: Modifier = Modifier,
    activeIndicator: @Composable () -> Unit,
    inactiveIndicator: @Composable () -> Unit,
    spacing: Dp = 8.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
) {
    val pageCount = state.pageCount
    Layout(
        modifier = modifier,
        content = {
            if (pageCount > 0) {
                val activeIndicatorModifier = Modifier
                    .layoutId(ActiveIndicatorId)

                Box(modifier = activeIndicatorModifier) {
                    activeIndicator()
                }
            }

            if (pageCount > 1) {
                val inactiveIndicatorModifier = Modifier
                    .layoutId(InactiveIndicatorId)

                repeat(pageCount - 1) {
                    Box(modifier = inactiveIndicatorModifier) {
                        inactiveIndicator()
                    }
                }
            }
        }
    ) { measurables, constraints ->

        // If there is no page, don't take up any space
        if (pageCount == 0) {
            return@Layout layout(0, 0) {}
        }

        val spacingPx = spacing.roundToPx()

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val activePlaceable =
            measurables.first { it.layoutId == ActiveIndicatorId }.measure(looseConstraints)
        val inactivePlaceables = measurables.filter { it.layoutId == InactiveIndicatorId }
            .map { it.measure(looseConstraints) }

        val inactiveIndicatorWidthPx = inactivePlaceables.maxOf { it.width }
        val activeIndicatorWidthPx = activePlaceable.width

        val width =
            activePlaceable.width + inactivePlaceables.sumOf { it.width } + (spacingPx * (pageCount - 1))
        val height = maxOf(activePlaceable.height, inactivePlaceables.maxOf { it.height })

        layout(width = width, height = height) {
            val currentPage = state.currentPage
            val offset = state.currentPageOffsetFraction

            val next = state.currentPage + offset.sign.toInt()

            val inactiveIterator = inactivePlaceables.iterator()

            // Current running x position
            var currentX = 0

            repeat(pageCount) { currentlyPlacingIndex ->
                val xOffset = when (currentlyPlacingIndex) {
                    // Currently placing indicator is active indicator
                    currentPage -> ((inactiveIndicatorWidthPx + spacingPx) * offset).roundToInt()
                    // Currently placing indicator is next indicator
                    next -> ((activeIndicatorWidthPx + spacingPx) * -offset).roundToInt()
                    // Currently placing indicator is neither active nor next indicator, so it is inactive non animating indicator
                    else -> 0
                }

                val currentlyPlacingActive = currentlyPlacingIndex == currentPage

                val currentPlaceable = if (currentlyPlacingActive) {
                    activePlaceable
                } else {
                    inactiveIterator.next()
                }

                currentPlaceable.placeRelative(
                    x = currentX + xOffset,
                    y = verticalAlignment.align(size = currentPlaceable.height, space = height),
                    zIndex = if (currentlyPlacingActive) 1f else 0f // Ensure active indicator is on top
                )

                currentX += currentPlaceable.width

                // Add spacing
                if (currentlyPlacingIndex != pageCount - 1) {
                    currentX += spacingPx
                }
            }
        }
    }
}

@Stable
private interface PagerStateBridge {

    val pageCount: Int
    val currentPage: Int
    val currentPageOffsetFraction: Float
}

private const val ActiveIndicatorId = "activeIndicatorId"
private const val InactiveIndicatorId = "inactiveIndicatorId"

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun PagerIndicatorPreview() {
    Box(Modifier.fillMaxSize()) {
        val state = rememberPagerState { 5 }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = state,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                text = (it + 1).toString()
            )
        }
        PagerIndicator(
            modifier = Modifier.align(Alignment.BottomCenter),
            pagerState = state,
            inactiveIndicatorWidth = 16.dp,
            activeIndicatorWidth = 5.dp,
            activeIndicatorHeight = 32.dp,
            spacing = 8.dp,
            verticalAlignment = Alignment.CenterVertically
        )
    }
}
