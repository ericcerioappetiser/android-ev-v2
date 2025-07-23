package com.appetiser.feature.song.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.components.BaseplateSearchBar
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Sizes
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.ui.R
import com.appetiser.feature.song.ui.components.BottomNavBar
import com.appetiser.feature.song.ui.components.BottomTab
import com.appetiser.feature.song.ui.components.SongItem

@Composable
fun ExploreScreen(
    songs: List<Song>,
    onSearch: (String) -> Unit,
    onToggleFavorite: (Song) -> Unit
) {
    val (searchValue, setSearchValue) = remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(BottomTab.Explore) }

    Scaffold(
        containerColor = BaseplateTheme.colors.surfaceBackground,
        bottomBar = {
            BottomNavBar(selectedTab) {
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
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
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().background(BaseplateTheme.colors.surfaceBackground),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(Margins.SMALL),
                horizontalArrangement = Arrangement.spacedBy(Margins.MICRO)
            ) {
                items(songs) { song ->
                    SongItem(
                        song = song,
                        onFavoriteClick = { onToggleFavorite(song) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ExploreScreenPreview() {
    ExploreScreen(
        listOf(
            Song(
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
            SSong(
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
            Song(
                id = "",
                title = "What it is to Burn",
                artistName = "Finch",
                artistUrl = "",
                longDescription = "Lorem ipsum",
                shortDescription = "Lorem ipsum",
                price = 100.00,
                thumbnail = "",
                videoUrl = ""
            )
        ),
        {}
    ) { }
}
