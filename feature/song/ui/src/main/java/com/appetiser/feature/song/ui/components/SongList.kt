package com.appetiser.feature.song.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.appetiser.core.common.compose.components.CustomEmptyState
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.song.data.utils.PagingState
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongList(
    modifier: Modifier,
    lazyPagingItems: LazyPagingItems<Song>,
    pagingState: PagingState,
    toNavigateSongDetails: (Song) -> Unit,
    onToggleFavorite: (Song) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = pagingState.isRefreshing,
        onRefresh = {
            lazyPagingItems.refresh()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        if (pagingState.isEmpty) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CustomEmptyState(
                    title = stringResource(R.string.explore),
                    subtitle = stringResource(R.string.no_songs_found)
                )
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BaseplateTheme.colors.surfaceBackground),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(Margins.SMALL),
                horizontalArrangement = Arrangement.spacedBy(Margins.MICRO)
            ) {
                items(
                    lazyPagingItems.itemCount,
                    key = { index -> lazyPagingItems[index]?.id ?: index }
                ) { index ->
                    val song = lazyPagingItems[index]
                    song?.let {
                        SongItem(
                            song = it,
                            onFavoriteClick = { onToggleFavorite(it) },
                            onClick = { toNavigateSongDetails(it) }
                        )
                    }
                }
            }
        }
    }
}
