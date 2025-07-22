package com.appetiser.core.common.compose.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun CustomAsyncImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CircleShape,
    placeholder: Painter? = null,
    error: Painter? = placeholder,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier.clip(shape),
        placeholder = placeholder,
        error = error,
        contentScale = contentScale
    )
}
