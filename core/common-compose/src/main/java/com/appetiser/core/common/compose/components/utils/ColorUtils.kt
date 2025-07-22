package com.appetiser.core.common.compose.components.utils

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ColorUtils {


    @Composable
    fun getResolvedColor(
        context: Context,
        isActive: Boolean,
        activeColor: Color,
        defaultColor: Color,
        colorLabel: Int
    ): Color {
        val label = context.getString(colorLabel)
        return animateColorAsState(
            targetValue = if (isActive) activeColor else defaultColor,
            label = label
        ).value
    }
}
