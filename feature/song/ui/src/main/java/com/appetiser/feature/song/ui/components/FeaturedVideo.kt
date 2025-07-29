package com.appetiser.feature.song.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Sizes
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.song.ui.R

@Composable
fun FeaturedVideo(videoThumbnailUrl: String, title: String, onPlayClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = Paddings.LARGE)
    ) {
        Text(
            stringResource(R.string.video),
            style = BaseplateTheme.typography.subHeadSemiBold
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Paddings.LARGE)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Sizes.SMALL)),
                model = videoThumbnailUrl,
                contentDescription = stringResource(R.string.cover, title),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = onPlayClicked,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(BaseplateTheme.colors.iconWhite.copy(alpha = 0.8f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.play),
                    tint = BaseplateTheme.colors.iconBrand
                )
            }
        }
    }
}


@Preview
@Composable
fun FeatureVideoPreview() {
    FeaturedVideo(
        videoThumbnailUrl = "",
        title = "Tews",
        onPlayClicked = {}
    )
}
