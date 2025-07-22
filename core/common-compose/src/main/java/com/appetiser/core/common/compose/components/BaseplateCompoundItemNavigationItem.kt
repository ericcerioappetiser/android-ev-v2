package com.appetiser.core.common.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun BaseplateCompoundItemNavigationItem(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    description: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    divider: @Composable (() -> Unit)? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    dividerPadding: Dp = 0.dp,
    leadingContentPadding: Dp = Paddings.SMALL,
    trailingContentPadding: Dp = Margins.LARGE,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent?.let {
                it()
            }

            Column(
                modifier = Modifier
                    .padding(start = if (leadingContent == null) 0.dp else leadingContentPadding)
            ) {
                title?.let {
                    it()
                }
                description?.let {
                    it()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            trailingContent?.let {
                Spacer(modifier = Modifier.width(trailingContentPadding))
                it()
            }
        }
        divider?.let {
            Box(modifier = Modifier.fillMaxWidth().padding(dividerPadding)) {
                it()
            }
        }
    }
}

@Composable
@Preview
fun CustomizeSettingItemPreview() {
    BaseplateTheme {
        BaseplateCompoundItemNavigationItem(
            modifier = Modifier
                .fillMaxWidth()
                .background(BaseplateTheme.colors.surfaceBackground)
                .padding(horizontal = Paddings.SMALL, vertical = Paddings.LARGE)
                .clickable {
                    // TODO handle click here
                },
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(id = R.string.settings_icon_description),
                    modifier = Modifier.size(24.dp)
                )
            },
            description = {
                Text(
                    text = stringResource(id = R.string.edit_profile_description),
                    style = BaseplateTheme.typography.bodyRegular,
                    color = BaseplateTheme.colors.textPrimary
                )
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = stringResource(id = R.string.settings_icon_description),
                    modifier = Modifier.size(24.dp),
                    tint = BaseplateTheme.colors.surfaceBackground
                )
            }
        )
    }
}

@Composable
@Preview
fun CustomizeSettingItemPreviewWithBorder() {
    BaseplateTheme {
        BaseplateCompoundItemNavigationItem(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BaseplateTheme.colors.borderDefault, shape = RoundedCornerShape(Radius.SMALL))
                .clip(RoundedCornerShape(Radius.SMALL))
                .background(BaseplateTheme.colors.surfaceBackground)
                .padding(horizontal = Paddings.SMALL, vertical = Paddings.LARGE)
                .background(BaseplateTheme.colors.surfaceBackground)
                .padding(horizontal = Paddings.SMALL, vertical = Paddings.LARGE)
                .clickable {
                    // TODO handle click here
                },
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(id = R.string.settings_icon_description),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.edit_profile),
                    style = BaseplateTheme.typography.bodyRegular,
                    color = BaseplateTheme.colors.textPrimary
                )
            },
            description = {
                Text(
                    text = stringResource(id = R.string.edit_profile_description),
                    style = BaseplateTheme.typography.footnoteRegular,
                    color = BaseplateTheme.colors.textSecondary
                )
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = stringResource(id = R.string.settings_icon_description),
                    modifier = Modifier.size(24.dp),
                    tint = BaseplateTheme.colors.surfaceBackground
                )
            }
        )
    }
}
