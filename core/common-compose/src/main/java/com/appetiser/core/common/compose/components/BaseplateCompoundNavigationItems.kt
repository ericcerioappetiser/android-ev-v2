package com.appetiser.core.common.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appetiser.baseplate.common.util.DividerFrequency
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.android.R as commonAndroid
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun <T : Enum<T>> BaseplateCompoundNavigationItems(
    modifier: Modifier = Modifier,
    enumClass: Class<T>,
    itemCount: Int = enumClass.enumConstants?.size ?: 0,
    itemContent: @Composable (index: T) -> Unit,
    divider: @Composable (() -> Unit)? = null,
    dividerSpaceBetween: Dp = Paddings.MICRO,
    onClick: (T) -> Unit,
    dividerFrequency: DividerFrequency = DividerFrequency.EVERY_ITEM
) {
    val items = enumClass.enumConstants

    Column(
        modifier = modifier
    ) {
        repeat(itemCount) { index ->
            val currentItem = items?.get(index)
            currentItem?.let {
                NavigationItem(
                    itemContent = itemContent,
                    divider = divider,
                    dividerSpaceBetween = dividerSpaceBetween,
                    dividerFrequency = dividerFrequency,
                    onClick = onClick,
                    itemCount = itemCount,
                    currentItem = currentItem,
                    index = index

                )
            }
        }
    }
}

@Composable
private fun <T : Enum<T>> NavigationItem(
    currentItem: T,
    itemContent: @Composable (item: T) -> Unit,
    divider: @Composable (() -> Unit)?,
    dividerSpaceBetween: Dp,
    dividerFrequency: DividerFrequency,
    onClick: (T) -> Unit,
    itemCount: Int,
    index: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onClick(currentItem)
            })
            .background(BaseplateTheme.colors.surfaceBackground)
    ) {
        itemContent(currentItem)

        if (shouldShowDivider(index = index, dividerFrequency = dividerFrequency.value, itemCount = itemCount)) {
            DividerSection(divider = divider, dividerSpaceBetween = dividerSpaceBetween)
        }
    }
}

@Composable
private fun DividerSection(
    divider: @Composable (() -> Unit)?,
    dividerSpaceBetween: Dp
) {
    Spacer(modifier = Modifier.height(dividerSpaceBetween))
    divider?.let {
        it()
    }
}


private fun shouldShowDivider(index: Int, dividerFrequency: Int, itemCount: Int): Boolean = (index + 1) % dividerFrequency == 0 && index < itemCount - 1

@Preview
@Composable
fun BaseplateCompoundNavigationItemsPreview() {
    BaseplateTheme {
        BaseplateCompoundNavigationItems(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BaseplateTheme.colors.borderDefault, shape = RoundedCornerShape(Radius.SMALL))
                .clip(RoundedCornerShape(Radius.SMALL))
                .background(BaseplateTheme.colors.surfaceBackground)
                .padding(horizontal = Paddings.SMALL, vertical = Paddings.SMALL),
            itemContent = { itemType ->
                when (itemType) {
                    FirstNavigateItem.EMAIL_ADDRESS -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    FirstNavigateItem.PASSWORD -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }
                }
            },
            divider = {
                HorizontalDividerDivider()
            },
            onClick = { selectedItem ->
                // TODO this where you handle the redirect based on selected item
            },
            enumClass = FirstNavigateItem::class.java
        )
    }
}

@Preview
@Composable
fun BaseplateCompoundNavigationItemsByTwoPreview() {
    BaseplateTheme {
        BaseplateCompoundNavigationItems(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BaseplateTheme.colors.borderDefault, shape = RoundedCornerShape(Radius.SMALL))
                .clip(RoundedCornerShape(Radius.SMALL))
                .background(BaseplateTheme.colors.surfaceBackground)
                .padding(horizontal = Paddings.SMALL, vertical = Paddings.SMALL),
            itemContent = { itemType ->
                when (itemType) {
                    NavigateItemType.CHANGE_EMAIL_ADDRESS -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    NavigateItemType.PASSWORD -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    NavigateItemType.LOGOUT -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    NavigateItemType.DELETE_ACCOUNT -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    NavigateItemType.DELETE_ACCOUNT1 -> {
                    }
                }
            },
            divider = {
                HorizontalDividerDivider()
            },
            onClick = { selectedItem ->
                // TODO this where you handle the redirect based on selected item
            },
            enumClass = NavigateItemType::class.java
        )
    }
}


@Preview
@Composable
fun BaseplateCompoundNavigationItemsEveryTwoPreview() {
    BaseplateTheme {
        BaseplateCompoundNavigationItems(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BaseplateTheme.colors.borderDefault, shape = RoundedCornerShape(Radius.SMALL))
                .clip(RoundedCornerShape(Radius.SMALL))
                .background(BaseplateTheme.colors.surfaceBackground)
                .padding(horizontal = Paddings.SMALL, vertical = Paddings.SMALL),
            dividerFrequency = DividerFrequency.EVERY_TWO_ITEMS,
            itemContent = { itemType ->
                when (itemType) {
                    NavigateItemType.CHANGE_EMAIL_ADDRESS -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    NavigateItemType.PASSWORD -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    NavigateItemType.LOGOUT -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    NavigateItemType.DELETE_ACCOUNT -> {
                        BaseplateNavigationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BaseplateTheme.colors.surfaceBackground)
                                .padding(Paddings.MEDIUM),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(id = commonAndroid.string.settings_icon_description),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            description = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    style = BaseplateTheme.typography.footnoteRegular,
                                    color = BaseplateTheme.colors.textSecondary
                                )
                            }
                        )
                    }

                    NavigateItemType.DELETE_ACCOUNT1 -> {
                    }
                }
            },
            divider = {
                HorizontalDividerDivider()
            }, onClick = {
            },
            enumClass = NavigateItemType::class.java
        )
    }
}

@Composable
private fun HorizontalDividerDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Paddings.SMALL),
        color = BaseplateTheme.colors.borderDefault
    )
}

@Composable
fun BaseplateNavigationItem(
    titleRes: Int = commonAndroid.string.email_address_label,
    arrowDescriptionRes: Int = commonAndroid.string.arrow_icon_description,
    onClick: (() -> Unit?)? = null,
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    description: @Composable (() -> Unit)? = null
) {
    BaseplateCompoundItemNavigationItem(
        modifier = modifier
            .clickable {
                onClick?.invoke()
            },
        title = {
            Text(
                text = stringResource(id = titleRes),
                style = BaseplateTheme.typography.bodyRegular,
                color = BaseplateTheme.colors.textPrimary
            )
        },
        leadingContent = {
            leadingContent?.invoke()
        },
        description = {
            description?.let {
                it()
            }
        },
        divider = {},
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = stringResource(id = arrowDescriptionRes),
                modifier = Modifier.size(Paddings.X_LARGE),
                tint = BaseplateTheme.colors.iconDefault
            )
        },
    )
}

enum class NavigateItemType {
    // TODO adjust this based on your items
    CHANGE_EMAIL_ADDRESS,
    PASSWORD,
    LOGOUT,
    DELETE_ACCOUNT,
    DELETE_ACCOUNT1,
}

enum class FirstNavigateItem {
    EMAIL_ADDRESS,
    PASSWORD
}
