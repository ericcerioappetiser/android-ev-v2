package com.appetiser.feature.song.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.components.CustomEmptyState
import com.appetiser.feature.song.ui.R

@Composable
fun SongDetailScreen(
    onBackClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CustomEmptyState(
            title = "test",
            subtitle = stringResource(R.string.no_songs_found)
        )
    }
}

@Preview
@Composable
fun SongDetailScreenPreview() {
    SongDetailScreen()
}
