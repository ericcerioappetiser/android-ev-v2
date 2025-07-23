package com.appetiser.feature.song.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.ui.R

@Composable
fun FavoriteIcon(
    modifier: Modifier,
    song: Song,
    onFavoriteClick: () -> Unit = {}
) {
    val painter = if (song.isFavorite) {
        painterResource(R.drawable.favorite)
    } else {
        painterResource(R.drawable.unfavorite)
    }

    val contentDescription = if (song.isFavorite) {
        stringResource(R.string.unfavorite)
    } else {
        stringResource(R.string.favorite)
    }

    IconButton(
        modifier = modifier,
        onClick = onFavoriteClick
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = Color.Unspecified
        )
    }
}
