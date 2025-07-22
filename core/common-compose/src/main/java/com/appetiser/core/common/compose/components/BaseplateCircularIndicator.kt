package com.appetiser.core.common.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun BaseplateCircularIndicator(
    modifier: Modifier = Modifier,
    color: Color = BaseplateTheme.colors.primaryDefault
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = color)
    }
}

