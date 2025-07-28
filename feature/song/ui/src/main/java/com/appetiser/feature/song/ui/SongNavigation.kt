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

@Serializable
data object SongListRoute

fun NavGraphBuilder.songListDestination(
    toSongDetail: (String) -> Unit
) {
    composable<SongListRoute> {
        SongListScreen {
            toSongDetail(it)
        }
    }
}

@Serializable
data class SongDetailRoute(val id: String)

fun NavGraphBuilder.songDetailDestination() {
    composable<SongDetailRoute> {
        SongDetailScreen()
    }
}
