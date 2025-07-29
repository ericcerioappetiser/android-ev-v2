package com.appetiser.androidecv.screens.main

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.appetiser.androidecv.screens.main.chat.ChatRoute
import com.appetiser.androidecv.screens.main.chat.chatDestination
import com.appetiser.androidecv.screens.main.home.HomeRoute
import com.appetiser.androidecv.screens.main.home.NestedRoute
import com.appetiser.androidecv.screens.main.home.homeDestination
import com.appetiser.androidecv.screens.main.newoption.NewDialog
import com.appetiser.androidecv.screens.main.newoption.newDestination
import com.appetiser.androidecv.screens.main.notification.notificationDestination
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.profile.ui.ProfileRoute
import com.appetiser.feature.profile.ui.profileGraph
import com.appetiser.feature.profile.ui.helpCentreDestination
import com.appetiser.feature.report.ui.ReportAProblemRoute
import com.appetiser.feature.report.ui.reportAProblemDestination
import com.appetiser.feature.song.ui.SongDetailRoute
import com.appetiser.feature.song.ui.SongListRoute
import com.appetiser.feature.song.ui.SongRoute
import com.appetiser.feature.song.ui.songDestination
import com.appetiser.feature.song.ui.songDetailDestination
import com.appetiser.feature.song.ui.songListDestination
import com.appetiser.feature.song.ui.R as SongR

data class MainScreenTopLevelRoute<T : Any>(val name: String, val route: T?, val icon: Painter)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val topLevelRoutes = listOf(
        MainScreenTopLevelRoute(
            stringResource(SongR.string.explore),
            SongListRoute,
            painterResource(R.drawable.ic_home)
        ),

        MainScreenTopLevelRoute(
            stringResource(SongR.string.favorites),
            null,
            painterResource(SongR.drawable.ic_heart)
        ),

        MainScreenTopLevelRoute(
            stringResource(R.string.profile),
            ProfileRoute,
            painterResource(R.drawable.ic_profile)
        ),
    )

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp), // we want the nav host routes to handle insets
        bottomBar = {
            val borderColor = BaseplateTheme.colors.borderDefault

            // visible if the current destination is not in the top level
            val visible = topLevelRoutes.any {
                if (it.route == null) {
                    return@any false
                }
                currentDestination?.hasRoute(it.route::class) == true
            }

            AnimatedVisibility(
                visible = visible,
                enter = expandVertically() + slideInVertically { it },
                exit = shrinkVertically() + slideOutVertically { it }
            ) {
                NavigationBar(
                    modifier = Modifier.drawWithContent {
                        drawContent()
                        drawLine(
                            color = borderColor,
                            start = Offset.Zero,
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                    containerColor = BaseplateTheme.colors.surfaceBackground
                ) {
                    val context = LocalContext.current

                    topLevelRoutes.forEach { topLevelRoute ->

                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(),
                            icon = {
                                Icon(
                                    topLevelRoute.icon,
                                    contentDescription = topLevelRoute.name
                                )
                            },
                            label = {
                                Text(
                                    topLevelRoute.name,
                                    style = BaseplateTheme.typography.navigationLabel
                                )
                            },
                            selected = currentDestination?.hierarchy?.any {
                                if (topLevelRoute.route == null) {
                                    return@any false
                                }
                                it.hasRoute(topLevelRoute.route::class)
                            } == true,
                            onClick = {
                                if (topLevelRoute.route == null) {
                                    // TODO: Show new dialog
                                    Toast.makeText(context, "New Clicked", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    navController.navigate(topLevelRoute.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding),
            navController = navController,
            startDestination = SongRoute,
        ) {
            homeDestination(
                onHome = {
                    // demos circular navigation, but with correct pop behavior
                    // i.e no duplicates, verify using back button
                    navController.navigate(HomeRoute) {
                        launchSingleTop = true
                        popUpTo(HomeRoute)
                    }
                },
                onNested = {
                    navController.navigate(NestedRoute)
                }
            )

            songDestination(navController)
            songListDestination {
                SongDetailRoute(it)
            }
            songDetailDestination(
                onBackClick = { navController.popBackStack() }
            )

            chatDestination()
            newDestination(navController)
            notificationDestination()
            profileGraph(navController = navController)
            helpCentreDestination(
                onBackClick = { navController.popBackStack() },
                onNavigateToReport = {
                    navController.navigate(ReportAProblemRoute)
                }
            )
            reportAProblemDestination(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
