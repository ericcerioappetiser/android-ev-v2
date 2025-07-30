package com.appetiser.feature.song.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.ui.R
import com.appetiser.feature.song.ui.components.SongDetailsContent

@Composable
fun SongDetailScreen(
    viewModel: SongDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        SongDetailScreen(
            state,
            onBackClick = onBackClick,
            onFavoriteClick = {}
        )
    }
}

@Composable
internal fun SongDetailScreen(
    state: SongDetailViewModel.ViewState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit = {}
) {
    val song = state.song

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Paddings.LARGE)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.back)
                )
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = stringResource(R.string.favorite)
                )
            }
        }

        SongDetailsContent(
            price = song?.price ?: 0.0,
            title = song?.title.orEmpty(),
            artist = song?.artistName.orEmpty(),
            description = song?.longDescription.orEmpty(),
            genre = song?.genre.orEmpty(),
            imageUrl = song?.thumbnail.orEmpty(),
        )
    }
}

@Preview
@Composable
fun SongDetailScreenPreview() {
    SongDetailScreen(
        state = SongDetailViewModel.ViewState(
            song = Song(
                id = 0,
                title = "What it is to Burn",
                artistName = "Finch",
                artistUrl = "",
                longDescription = "Lorem ipsum",
                shortDescription = "Lorem ipsum",
                price = 100.00,
                thumbnail = "",
                genre = "",
                videoUrl = ""
            )
        ),
        {},
        {}
    )
}
