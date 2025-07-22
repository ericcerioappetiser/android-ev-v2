package com.appetiser.core.common.compose.components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme

private val ContainerMaxWidth = 600.dp // from material3 source code

/**
 * A composable function that displays a snackbar with custom styling based on the provided [BaseplateSnackbarVisuals].
 * This function acts as a wrapper around different custom snackbar implementations to adhere to the Baseplate specification.
 *
 * This function determines which snackbar implementation to use based on the specific [BaseplateSnackbarVisuals] subtype
 * within the provided [snackbarData].
 *  The type is defined using the sealed class instance type of [BaseplateSnackbarVisuals] and can be one of:
 *   - [BaseplateSnackbarVisuals.Error]: Displays an error snackbar.
 *   - [BaseplateSnackbarVisuals.Info]: Displays a general information snackbar.
 *   - [BaseplateSnackbarVisuals.Success]: Displays a success snackbar.
 *   - [BaseplateSnackbarVisuals.InfoCustom]: Displays a custom info snackbar (handled by [BaseplateInfoCustomSnackbar]).
 *
 * This function enforces that the provided [snackbarData]'s visuals are of type [BaseplateSnackbarVisuals], throwing an exception otherwise.
 * This ensures consistent styling and handling of snackbar visuals within the Baseplate design system.
 *
 * @param modifier Modifier to be applied to the snackbar.
 * @param snackbarData Data containing the content and actions of the snackbar. The visuals of this [snackbarData]
 * **must** be of type [BaseplateSnackbarVisuals] to be rendered correctly. The specific [BaseplateSnackbarVisuals]
 * subtype determines which custom style is applied.
 *
 * @throws IllegalArgumentException if the provided [snackbarData]'s visuals are not of type [BaseplateSnackbarVisuals].
 */
@Composable
fun BaseplateSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val snackbarVisuals = requireNotNull(snackbarData.visuals as? BaseplateSnackbarVisuals) {
        "SnackbarVisuals must be of type BaseplateSnackbarVisuals"
    }

    if (snackbarVisuals !is BaseplateSnackbarVisuals.InfoCustom) {
        val themeColors = BaseplateTheme.colors

        val (containerColor, contentColor) =
            @Suppress("KotlinConstantConditions") // workaround, see details on InfoCustom `when` branch
            when (snackbarVisuals) {
                is BaseplateSnackbarVisuals.Error -> themeColors.semanticErrorDefault to themeColors.semanticErrorShade
                is BaseplateSnackbarVisuals.Info -> themeColors.semanticInfoDefault to themeColors.semanticInfoShade
                is BaseplateSnackbarVisuals.Success -> themeColors.semanticSuccess to themeColors.semanticSuccessShade
                is BaseplateSnackbarVisuals.InfoCustom -> {
                    // this should never be reached https://youtrack.jetbrains.com/issue/KTIJ-20749/Exhaustive-when-check-does-not-take-into-account-the-values-excluded-by-previous-if-conditions
                    // fallback as Info instead
                    themeColors.semanticInfoDefault to themeColors.semanticInfoShade
                }
            }

        Box(
            modifier = modifier
                .padding(Paddings.MEDIUM)
                .sizeIn(maxWidth = ContainerMaxWidth)
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.SMALL))
                .background(containerColor)
                .padding(horizontal = Paddings.LARGE, vertical = Paddings.MEDIUM)
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = snackbarVisuals.message,
                        style = BaseplateTheme.typography.subHeadRegular,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(Margins.LARGE))
                    @Suppress("KotlinConstantConditions") // workaround, see details on InfoCustom `when` branch
                    val icon = when (snackbarVisuals) {
                        is BaseplateSnackbarVisuals.Error -> painterResource(R.drawable.ic_snackbar_error)
                        is BaseplateSnackbarVisuals.Info -> painterResource(R.drawable.ic_snackbar_info)
                        is BaseplateSnackbarVisuals.Success -> painterResource(R.drawable.ic_snackbar_success)
                        is BaseplateSnackbarVisuals.InfoCustom -> {
                            // this should never be reached https://youtrack.jetbrains.com/issue/KTIJ-20749/Exhaustive-when-check-does-not-take-into-account-the-values-excluded-by-previous-if-conditions
                            // fallback as Info instead
                            painterResource(R.drawable.ic_snackbar_info)
                        }
                    }
                    Icon(
                        painter = icon,
                        contentDescription = snackbarVisuals.message
                    )
                }
            }
        }
    } else {
        BaseplateInfoCustomSnackbar(
            snackbarVisuals = snackbarVisuals,
            onPerformAction = snackbarData::performAction,
            modifier = modifier
        )
    }
}

@Composable
private fun BaseplateInfoCustomSnackbar(
    snackbarVisuals: BaseplateSnackbarVisuals.InfoCustom,
    onPerformAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(Paddings.MEDIUM)
            .sizeIn(maxWidth = ContainerMaxWidth)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BaseplateTheme.colors.borderDefault,
                shape = RoundedCornerShape(Radius.SMALL)
            )
            .clip(RoundedCornerShape(Radius.SMALL))
            .background(BaseplateTheme.colors.surfaceBackground)
            .padding(Paddings.LARGE)
    ) {
        CompositionLocalProvider(LocalContentColor provides BaseplateTheme.colors.textPrimary) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                snackbarVisuals.icon?.let { icon ->
                    CompositionLocalProvider(
                        value = LocalContentColor provides BaseplateTheme.colors.iconSecondary,
                        content = icon
                    )
                    Spacer(Modifier.width(Margins.LARGE))
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = snackbarVisuals.message,
                    style = BaseplateTheme.typography.subHeadRegular,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                snackbarVisuals.action?.let { action ->
                    Spacer(Modifier.width(Margins.LARGE))
                    CompositionLocalProvider(
                        value = LocalContentColor provides BaseplateTheme.colors.textBrand,
                    ) {
                        action(onPerformAction)
                    }
                }
            }
        }
    }
}

@Preview(name = "Info Snackbar")
@Composable
private fun BaseplateSnackbarInfoPreview() {
    BaseplateSnackbar(
        snackbarData = object : SnackbarData {
            override val visuals: SnackbarVisuals = BaseplateSnackbarVisuals.Info(
                message = "This is an info snackbar",
                duration = SnackbarDuration.Short
            )

            override fun dismiss() = Unit
            override fun performAction() = Unit
        }
    )
}

@Preview(name = "Success Snackbar")
@Composable
private fun BaseplateSnackbarSuccessPreview() {
    BaseplateSnackbar(
        snackbarData = object : SnackbarData {
            override val visuals: SnackbarVisuals = BaseplateSnackbarVisuals.Success(
                message = "This is a success snackbar",
                duration = SnackbarDuration.Short
            )

            override fun dismiss() = Unit
            override fun performAction() = Unit
        }
    )
}

@Preview(name = "Error Snackbar")
@Composable
private fun BaseplateSnackbarErrorPreview() {
    BaseplateSnackbar(
        snackbarData = object : SnackbarData {
            override val visuals: SnackbarVisuals = BaseplateSnackbarVisuals.Error(
                message = "This is an error snackbar",
                duration = SnackbarDuration.Short
            )

            override fun dismiss() = Unit
            override fun performAction() = Unit
        }
    )
}

@Preview(name = "Info Custom Snackbar", group = "Info custom")
@Composable
private fun BaseplateSnackbarInfoCustomPreview() {
    BaseplateSnackbar(
        snackbarData = object : SnackbarData {
            override val visuals: SnackbarVisuals = BaseplateSnackbarVisuals.InfoCustom(
                message = "This is a custom info snackbar",
                duration = SnackbarDuration.Short,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_snackbar_info),
                        contentDescription = "Info icon"
                    )
                },
                action = {
                    Text(text = "Action")
                }
            )

            override fun dismiss() = Unit
            override fun performAction() = Unit
        }
    )
}

@Preview(name = "Info Custom Snackbar, No Icon", group = "Info custom")
@Composable
private fun BaseplateSnackbarInfoCustomNoIconPreview() {
    BaseplateSnackbar(
        snackbarData = object : SnackbarData {
            override val visuals: SnackbarVisuals = BaseplateSnackbarVisuals.InfoCustom(
                message = "This is a custom info snackbar without an icon",
                duration = SnackbarDuration.Short,
                icon = null,
                action = {
                    Text(text = "Action")
                }
            )

            override fun dismiss() = Unit
            override fun performAction() = Unit
        }
    )
}

@Preview(name = "Info Custom Snackbar, No Action", group = "Info custom")
@Composable
private fun BaseplateSnackbarInfoCustomNoActionPreview() {
    BaseplateSnackbar(
        snackbarData = object : SnackbarData {
            override val visuals: SnackbarVisuals = BaseplateSnackbarVisuals.InfoCustom(
                message = "This is a custom info snackbar without an action",
                duration = SnackbarDuration.Short,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_snackbar_info),
                        contentDescription = "Info icon"
                    )
                },
                action = null
            )

            override fun dismiss() = Unit
            override fun performAction() = Unit
        }
    )
}

@Preview(name = "Info Custom Snackbar, No Icon, No Action", group = "Info custom")
@Composable
private fun BaseplateSnackbarInfoCustomNoIconNoActionPreview() {
    BaseplateSnackbar(
        snackbarData = object : SnackbarData {
            override val visuals: SnackbarVisuals = BaseplateSnackbarVisuals.InfoCustom(
                message = "This is a custom info snackbar without an icon or action",
                duration = SnackbarDuration.Short,
                icon = null,
                action = null
            )

            override fun dismiss() = Unit
            override fun performAction() = Unit
        }
    )
}


