package com.appetiser.feature.song.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.appetiser.feature.song.ui.details.SongDetailScreen
import com.appetiser.feature.song.ui.list.SongListScreen
import kotlinx.serialization.Serializable

@Serializable
data object SongRoute

@Serializable
data object SongListRoute

@Serializable
data class SongDetailRoute(val id: Int)


fun NavGraphBuilder.songDestination(
    navController: NavController
) {
    navigation<SongRoute>(
        startDestination = SongListRoute
    ) {
        songListDestination(
            toSongDetail = {
                navController.navigate(SongDetailRoute(it))
            }
        )
    }
}

fun NavGraphBuilder.songListDestination(
    toSongDetail: (Int) -> Unit
) {
    composable<SongListRoute> {
        SongListScreen {
            toSongDetail(it)
        }
    }
}

fun NavGraphBuilder.songDetailDestination(
    onBackClick: () -> Unit
) {
    composable<SongDetailRoute> {
        SongDetailScreen(
            onBackClick = onBackClick
        )
    }
}
