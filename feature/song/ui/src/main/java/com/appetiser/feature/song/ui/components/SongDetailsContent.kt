package com.appetiser.feature.song.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun SongDetailsContent(
    price: Double,
    title: String,
    artist: String,
    description: String,
    genre: String,
    imageUrl: String
) {
    Column(
        modifier = Modifier
            .padding(Paddings.SMALL)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Sizes.SMALL)),
            model = imageUrl,
            contentDescription = stringResource(R.string.cover, title),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier
                .height(Sizes.SMALL)
        )

        Text(
            modifier = Modifier.padding(top = Paddings.LARGE),
            text = "$${"%.2f".format(price)}",
            style = BaseplateTheme.typography.title3
        )
        Text(
            text = title,
            style = BaseplateTheme.typography.bodyRegular,
            color = BaseplateTheme.colors.textPrimary
        )
        Row {
            Text(
                text = artist,
                style = BaseplateTheme.typography.bodyRegular,
                color = BaseplateTheme.colors.textTertiary
            )

            Text(
                text = stringResource(R.string.dash),
                style = BaseplateTheme.typography.bodyRegular,
                color = BaseplateTheme.colors.textTertiary
            )

            Text(
                text = genre,
                style = BaseplateTheme.typography.bodyRegular,
                color = BaseplateTheme.colors.textTertiary
            )
        }


        HorizontalDivider(
            modifier = Modifier.padding(vertical = Paddings.LARGE),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )

        Text(
            text = stringResource(R.string.description),
            style = BaseplateTheme.typography.subHeadSemiBold,
            color = BaseplateTheme.colors.textSecondary,
        )

        Text(
            modifier = Modifier.padding(top = Paddings.SMALL),
            text = description,
            style = BaseplateTheme.typography.subHeadRegular,
            color = BaseplateTheme.colors.textSecondary
        )
    }
}

@Preview
@Composable
fun SongDetailsPreview() {
    SongDetailsContent(
        0.0,
        "Tews",
        "ERR",
        "Lorem Ipsum this is a long description",
        "test",
        imageUrl = ""
    )
}
