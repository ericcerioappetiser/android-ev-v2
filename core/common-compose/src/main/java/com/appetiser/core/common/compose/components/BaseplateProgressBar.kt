package com.appetiser.core.common.compose.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme
import kotlin.math.roundToInt

@Composable
fun BaseplateProgressBar(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    color: Color = BaseplateTheme.colors.primaryDefault,
    trackColor: Color = BaseplateTheme.colors.borderDefault,
    shape: Shape = RoundedCornerShape(Radius.LARGE),

) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Margins.SMALL)
            .clip(shape),

    ) {
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            visibilityThreshold = 0.001f,
            label = "Progress Animation"
        )

        Box(
            modifier = Modifier
                .layout { measurable, constraints ->
                    // Use layout lambda to efficiently animate the progress
                    val newWidth = (constraints.maxWidth * animatedProgress)
                        .roundToInt()
                        .coerceIn(constraints.minWidth, constraints.maxWidth)

                    val newHeight = constraints.maxHeight

                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = newWidth,
                            maxWidth = newWidth,
                            minHeight = newHeight,
                            maxHeight = newHeight
                        )
                    )

                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .background(color, shape)
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    clip = true
                }
        ) {
            drawRect(
                color = trackColor,
                topLeft = Offset.Zero,
                size = size,
            )

            drawCircle(
                color = Color.Black,
                radius = Radius.SMALL.toPx(),
                center = Offset(-5.dp.toPx(), size.height / 2),
                blendMode = BlendMode.Clear
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun ProgressBarPreview() {
    BaseplateTheme {
        Column(
            modifier = Modifier.padding(Paddings.LARGE)
        ) {
            val progress by remember {
                mutableFloatStateOf(0.5f)
            }

            val height = Modifier.height(Margins.SMALL)
            BaseplateProgressBar(
                modifier = height,
                progress = progress
            )
        }
    }
}
