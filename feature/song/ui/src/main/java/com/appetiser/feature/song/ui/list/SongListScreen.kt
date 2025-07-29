package com.appetiser.feature.song.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.appetiser.core.common.compose.components.BaseplateSearchBar
import com.appetiser.core.common.compose.components.CustomEmptyState
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Sizes
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.feature.song.data.utils.collectPagingState
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.ui.R
import com.appetiser.feature.song.ui.components.SongItem
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

@Composable
fun SongListScreen(
    viewModel: SongListViewModel = hiltViewModel(),
    onNavigateToSongDetails: (Int) -> Unit
) {
    val state by viewModel.collectAsState()
    SongListScreen(
        state,
        toNavigateSongDetails = { viewModel.action(SongListViewModel.ActionState.ClickSongItem(it.id)) },
        onToggleFavorite = { viewModel.action(SongListViewModel.ActionState.ToggleFavorite(it)) }
    )

    ConsumableSideEffect(state.onNavigateTo) {
        when (it) {
            SongListViewModel.NavigateDestination.SONG_DETAILS -> onNavigateToSongDetails(state.clickSongId ?: 0)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    state: SongListViewModel.ViewState,
    toNavigateSongDetails: (Song) -> Unit,
    onToggleFavorite: (Song) -> Unit
) {
    val lazyPagingItems = state.songs.collectAsLazyPagingItems()
    val (searchValue, setSearchValue) = remember { mutableStateOf("") }

    val pagingState = lazyPagingItems.collectPagingState()
    LaunchedEffect(pagingState.error) {
        if (pagingState.error != null) {
            Timber.e(pagingState.error?.message.orEmpty())
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddings ->
        Column(
            modifier = Modifier
                .padding(paddings)
                .padding(horizontal = Paddings.LARGE)
                .background(BaseplateTheme.colors.surfaceBackground),
        ) {
            Spacer(Modifier.height(Sizes.LARGE))
            Text(
                modifier = Modifier.padding(horizontal = Paddings.SMALL),
                text = "Explore",
                style = BaseplateTheme.typography.title1.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(Sizes.MEDIUM))

            BaseplateSearchBar(
                modifier = Modifier.fillMaxWidth().padding(horizontal = Paddings.SMALL),
                backgroundColor = BaseplateTheme.colors.surfaceFieldTags,
                value = searchValue,
                onValueChanged = setSearchValue,
                placeholder = { Text(text = stringResource(R.string.search)) }
            )

            Spacer(modifier = Modifier.height(Sizes.MEDIUM))

            PullToRefreshBox(
                isRefreshing = pagingState.isRefreshing,
                onRefresh = {
                    lazyPagingItems.refresh()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (pagingState.isEmpty) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomEmptyState(
                            title = stringResource(R.string.explore),
                            subtitle = stringResource(R.string.no_songs_found)
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize().background(BaseplateTheme.colors.surfaceBackground),
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
    }
}

@Preview
@Composable
fun ExploreScreenPreview() {
    SongListScreen(
        state = SongListViewModel.ViewState(
            songs = flowOf(
                PagingData.from(
                    listOf(
                        Song(
                            id = 0,
                            title = "What it is to Burn",
                            artistName = "Finch",
                            artistUrl = "",
                            longDescription = "Lorem ipsum",
                            shortDescription = "Lorem ipsum",
                            price = 100.00,
                            thumbnail = "",
                            videoUrl = ""
                        ),
                        Song(
                            id = 1,
                            title = "What it is to Burn",
                            artistName = "Finch",
                            artistUrl = "",
                            longDescription = "Lorem ipsum",
                            shortDescription = "Lorem ipsum",
                            price = 100.00,
                            thumbnail = "",
                            videoUrl = ""
                        ),
                        Song(
                            id = 2,
                            title = "What it is to Burn",
                            artistName = "Finch",
                            artistUrl = "",
                            longDescription = "Lorem ipsum",
                            shortDescription = "Lorem ipsum",
                            price = 100.00,
                            thumbnail = "",
                            videoUrl = ""
                        )
                    )
                )
            )
        ),
        {}
    ) { }
}
