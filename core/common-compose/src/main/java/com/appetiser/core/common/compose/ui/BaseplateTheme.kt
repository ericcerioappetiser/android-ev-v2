package com.appetiser.core.common.compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import com.appetiser.core.common.compose.dimensions.Radius

/**
 * Baseplate defined theme. This is the entry point for all the theming in the app.
 * This theme will bridge with the Material theme and provide the necessary colors and typography.
 *
 * @param colors The [BaseplateColors] to use for this theme.
 * @param typography The [BaseplateTypography] to use for this theme.
 * @param shapes The [Shapes] to use for this theme.
 */
@Composable
fun BaseplateTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    colors: BaseplateColors = if (isDarkTheme) darkBaseplateTheme() else lightBaseplateTheme(),
    typography: BaseplateTypography = BaseplateTheme.typography,
    shapes: Shapes = Shapes(
        small = RoundedCornerShape(Radius.SMALL),
        medium = RoundedCornerShape(Radius.SMALL),
        large = RoundedCornerShape(Radius.SMALL)
    ),
    content: @Composable () -> Unit
) {
    val materialColors = if (isDarkTheme) {
        darkColorScheme(
            primary = colors.primaryDefault,
            onPrimary = colors.textInverted,
            primaryContainer = colors.primaryShade2,
            onPrimaryContainer = colors.textInverted,
            secondary = colors.primaryShade1,
            onSecondary = colors.textInverted,
            secondaryContainer = colors.primaryShade3,
            onSecondaryContainer = colors.surfaceBrand,
            tertiary = colors.complementaryDefault,
            onTertiary = colors.textWhite,
            tertiaryContainer = colors.complementaryShade2,
            onTertiaryContainer = colors.complementaryDefault,
            background = colors.surfaceBackground,
            onBackground = colors.textPrimary,
            surface = colors.surfaceBackground,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.surfaceBackground3,
            onSurfaceVariant = colors.textTertiary,
            inverseSurface = colors.surfaceInverted,
            inverseOnSurface = colors.textInverted,
            error = colors.semanticErrorDefault,
            onError = colors.textWhite,
            errorContainer = colors.semanticErrorShade,
            onErrorContainer = colors.semanticErrorDefault,
            outline = colors.borderDefault,
            outlineVariant = colors.borderDisabled,
            surfaceContainer = colors.surfaceFieldTags,
            surfaceTint = colors.surfaceDisabled,
            inversePrimary = colors.iconDefault,
            surfaceBright = colors.semanticSuccess,
            surfaceContainerLow = colors.semanticSuccessShade
        )
    } else {
        lightColorScheme(
            primary = colors.primaryDefault,
            onPrimary = colors.textInverted,
            primaryContainer = colors.primaryShade2,
            onPrimaryContainer = colors.textInverted,
            secondary = colors.primaryShade1,
            onSecondary = colors.textInverted,
            secondaryContainer = colors.primaryShade3,
            onSecondaryContainer = colors.surfaceBrand,
            tertiary = colors.complementaryDefault,
            onTertiary = colors.textWhite,
            tertiaryContainer = colors.complementaryShade2,
            onTertiaryContainer = colors.complementaryDefault,
            background = colors.surfaceBackground,
            onBackground = colors.textPrimary,
            surface = colors.surfaceBackground,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.surfaceBackground3,
            onSurfaceVariant = colors.textTertiary,
            inverseSurface = colors.surfaceInverted,
            inverseOnSurface = colors.textInverted,
            error = colors.semanticErrorDefault,
            onError = colors.textWhite,
            errorContainer = colors.semanticErrorShade,
            onErrorContainer = colors.semanticErrorDefault,
            outline = colors.borderDefault,
            outlineVariant = colors.borderDisabled,
            surfaceContainer = colors.surfaceFieldTags,
            surfaceTint = colors.surfaceDisabled,
            inversePrimary = colors.iconDefault,
            surfaceBright = colors.semanticSuccess,
            surfaceContainerLow = colors.semanticSuccessShade
        )
    }

    val materialTypography = Typography(
        displayLarge = typography.largeTitle,
        displayMedium = typography.title1,
        displaySmall = typography.title2,
        headlineLarge = typography.title3,
        bodyLarge = typography.bodyBold,
        bodyMedium = typography.bodySemiBold,
        bodySmall = typography.bodyRegular,
        titleMedium = typography.subHeadSemiBold,
        titleSmall = typography.subHeadRegular,
        labelLarge = typography.captionBold,
        labelMedium = typography.captionRegular,
        labelSmall = typography.footnoteRegular
    )

    // We want to remember the colors so that if they change, we don't re-create the whole colors while still
    // notifying the composition that something has changed (via internal mutableStateOf).
    val rememberedColors = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }.apply { updateColorsFrom(colors) }

    MaterialTheme(
        colorScheme = materialColors,
        typography = materialTypography,
        shapes = shapes
    ) {
        CompositionLocalProvider(
            LocalBaseplateColors provides rememberedColors,
            LocalBaseplateTypography provides typography,
            content = content
        )
    }
}

object BaseplateTheme {

    /**
     * Retrieves the current [BaseplateColors] at the call site's position in the hierarchy.
     */
    val colors: BaseplateColors
        @Composable
        @ReadOnlyComposable
        get() = LocalBaseplateColors.current

    /**
     * Retrieves the current [BaseplateTypography] at the call site's position in the hierarchy.
     */
    val typography: BaseplateTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalBaseplateTypography.current

    /**
     * Retrieves the current [Shapes] at the call site's position in the hierarchy.
     * This is the same as [MaterialTheme.shapes].
     */
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes
}
