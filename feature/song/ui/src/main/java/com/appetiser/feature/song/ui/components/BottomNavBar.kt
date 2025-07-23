package com.appetiser.feature.song.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.song.ui.R

@Composable
fun BottomNavBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    Column {
        HorizontalDivider(
            Modifier,
            thickness = 1.dp,
            color = Color.LightGray
        )
        NavigationBar(
            containerColor = Color.White
        ) {
            BottomTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            painter = painterResource(id = tab.iconRes),
                            contentDescription = tab.label,
                            tint = if (isSelected) BaseplateTheme.colors.iconBrand else Color.Unspecified
                        )
                    },
                    label = {
                        Text(
                            text = tab.label,
                            color = if (isSelected) BaseplateTheme.colors.iconBrand else Color.Unspecified
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

enum class BottomTab(
    val label: String,
    @DrawableRes val iconRes: Int
) {
    Explore("Explore", R.drawable.ic_home),
    Favourites("Favourites", R.drawable.ic_heart),
    Profile("Profile", R.drawable.ic_profile)
}
