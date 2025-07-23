package com.appetiser.feature.song.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Sizes
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.ui.R

@Composable
fun FavoriteIcon(
    modifier: Modifier,
    song: Song,
    onFavoriteClick: () -> Unit = {}
) {
    val painter = if (song.isFavorite) {
        painterResource(R.drawable.ic_favorite)
    } else {
        painterResource(R.drawable.ic_unfavorite)
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

@Preview
@Composable
fun FavoriteIconPreview() {
    FavoriteIcon(
        modifier = Modifier
            .padding(Paddings.MICRO)
            .size(Sizes.XXX_LARGE),
        song = Song(
            id = "",
            title = "What it is to Burn",
            artistName = "Finch",
            artistUrl = "",
            longDescription = "Lorem ipsum",
            shortDescription = "Lorem ipsum",
            price = 100.00,
            thumbnail = "",
            videoUrl = ""
        ),
        onFavoriteClick = {}
    )
}
