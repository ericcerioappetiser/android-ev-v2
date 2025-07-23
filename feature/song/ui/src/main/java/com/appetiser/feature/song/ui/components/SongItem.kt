package com.appetiser.feature.song.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Sizes
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.ui.R

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song,
    onFavoriteClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Paddings.SMALL)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Sizes.MEDIUM))
                .background(Color.Red)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(Sizes.SMALL)),
                model = song.thumbnail,
                contentDescription = stringResource(R.string.cover, song.title),
                contentScale = ContentScale.Crop
            )
            FavoriteIcon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Paddings.MICRO)
                    .size(Sizes.XXX_LARGE),
                song = song,
                onFavoriteClick = onFavoriteClick
            )
        }
        Spacer(modifier = Modifier.height(Sizes.SMALL))
        Text(
            text = song.price,
            color = BaseplateTheme.colors.textPrimary,
            style = BaseplateTheme.typography.bodyBold
        )
        Text(
            text = song.title,
            color = BaseplateTheme.colors.textPrimary,
            style = BaseplateTheme.typography.subHeadRegular,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = song.artistName,
            color = BaseplateTheme.colors.textTertiary,
            style = BaseplateTheme.typography.subHeadRegular,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun SongCardPreview() {
    SongItem(
        modifier = Modifier,
        Song(
            id = "",
            title = "What it is to Burn",
            artistName = "Finch",
            description = "Lorem ipsum",
            price = "$100.00",
            thumbnail = "",
            videoUrl = ""
        )
    ) {
    }
}



