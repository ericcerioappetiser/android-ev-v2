package com.appetiser.core.common.compose.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

fun Modifier.dottedBorder(
    width: Dp,
    color: Color,
    radius: Dp,
    interval: Dp,
) = this.drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(width = width.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(interval.toPx(), interval.toPx()))),
        cornerRadius = CornerRadius(
            radius.toPx()
        )
    )
}
