package com.appetiser.core.common.compose.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Baseplate defined colors.
 */
@Stable
class BaseplateColors(
    primaryDefault: Color,
    primaryShade1: Color,
    primaryShade2: Color,
    primaryShade3: Color,
    textPrimary: Color,
    textSecondary: Color,
    textTertiary: Color,
    textDisabled: Color,
    textInverted: Color,
    textWhite: Color,
    textBrand: Color,
    surfaceBackground: Color,
    surfaceBackground2: Color,
    surfaceBackground3: Color,
    surfaceFieldTags: Color,
    surfaceDisabled: Color,
    surfaceInverted: Color,
    surfaceBrand: Color,
    surfaceBrand1: Color,
    surfaceBrand2: Color,
    surfaceBrand3: Color,
    borderDefault: Color,
    borderDisabled: Color,
    borderSecondary: Color,
    borderBrand: Color,
    borderInverted: Color,
    iconDefault: Color,
    iconSecondary: Color,
    iconTertiary: Color,
    iconDisabled: Color,
    iconInverted: Color,
    iconWhite: Color,
    iconBrand: Color,
    complementaryDefault: Color,
    complementaryShade1: Color,
    complementaryShade2: Color,
    complementaryShade3: Color,
    semanticSuccess: Color,
    semanticShade: Color,
    semanticInfoDefault: Color,
    semanticInfoShade: Color,
    semanticWarningDefault: Color,
    semanticWarningShade: Color,
    semanticErrorDefault: Color,
    semanticErrorShade: Color,
    semanticSuccessShade: Color,
) {

    var primaryDefault: Color by mutableStateOf(primaryDefault)
        internal set
    var primaryShade1: Color by mutableStateOf(primaryShade1)
        internal set
    var primaryShade2: Color by mutableStateOf(primaryShade2)
        internal set
    var primaryShade3: Color by mutableStateOf(primaryShade3)
        internal set
    var textPrimary: Color by mutableStateOf(textPrimary)
        internal set
    var textSecondary: Color by mutableStateOf(textSecondary)
        internal set
    var textTertiary: Color by mutableStateOf(textTertiary)
        internal set
    var textDisabled: Color by mutableStateOf(textDisabled)
        internal set
    var textInverted: Color by mutableStateOf(textInverted)
        internal set
    var textWhite: Color by mutableStateOf(textWhite)
        internal set
    var textBrand: Color by mutableStateOf(textBrand)
        internal set
    var surfaceBackground: Color by mutableStateOf(surfaceBackground)
        internal set
    var surfaceBackground2: Color by mutableStateOf(surfaceBackground2)
        internal set
    var surfaceBackground3: Color by mutableStateOf(surfaceBackground3)
        internal set
    var surfaceFieldTags: Color by mutableStateOf(surfaceFieldTags)
        internal set
    var surfaceDisabled: Color by mutableStateOf(surfaceDisabled)
        internal set
    var surfaceInverted: Color by mutableStateOf(surfaceInverted)
        internal set
    var surfaceBrand: Color by mutableStateOf(surfaceBrand)
        internal set
    var surfaceBrand1: Color by mutableStateOf(surfaceBrand1)
        internal set
    var surfaceBrand2: Color by mutableStateOf(surfaceBrand2)
        internal set
    var surfaceBrand3: Color by mutableStateOf(surfaceBrand3)
        internal set
    var borderDefault: Color by mutableStateOf(borderDefault)
        internal set
    var borderDisabled: Color by mutableStateOf(borderDisabled)
        internal set
    var borderSecondary: Color by mutableStateOf(borderSecondary)
        internal set
    var borderBrand: Color by mutableStateOf(borderBrand)
        internal set
    var borderInverted: Color by mutableStateOf(borderInverted)
        internal set
    var iconDefault: Color by mutableStateOf(iconDefault)
        internal set
    var iconSecondary: Color by mutableStateOf(iconSecondary)
        internal set
    var iconTertiary: Color by mutableStateOf(iconTertiary)
        internal set
    var iconDisabled: Color by mutableStateOf(iconDisabled)
        internal set
    var iconInverted: Color by mutableStateOf(iconInverted)
        internal set
    var iconWhite: Color by mutableStateOf(iconWhite)
        internal set
    var iconBrand: Color by mutableStateOf(iconBrand)
        internal set
    var complementaryDefault: Color by mutableStateOf(complementaryDefault)
        internal set
    var complementaryShade1: Color by mutableStateOf(complementaryShade1)
        internal set
    var complementaryShade2: Color by mutableStateOf(complementaryShade2)
        internal set
    var complementaryShade3: Color by mutableStateOf(complementaryShade3)
        internal set
    var semanticSuccess: Color by mutableStateOf(semanticSuccess)
        internal set
    var semanticShade: Color by mutableStateOf(semanticShade)
        internal set
    var semanticInfoDefault: Color by mutableStateOf(semanticInfoDefault)
        internal set
    var semanticInfoShade: Color by mutableStateOf(semanticInfoShade)
        internal set
    var semanticWarningDefault: Color by mutableStateOf(semanticWarningDefault)
        internal set
    var semanticWarningShade: Color by mutableStateOf(semanticWarningShade)
        internal set
    var semanticErrorDefault: Color by mutableStateOf(semanticErrorDefault)
        internal set
    var semanticErrorShade: Color by mutableStateOf(semanticErrorShade)
        internal set
    var semanticSuccessShade: Color by mutableStateOf(semanticSuccessShade)
        internal set


    /**
     * Returns a copy of this Colors, optionally overriding some of the values.
     */
    fun copy(
        primaryDefault: Color = this.primaryDefault,
        primaryShade1: Color = this.primaryShade1,
        primaryShade2: Color = this.primaryShade2,
        primaryShade3: Color = this.primaryShade3,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        textTertiary: Color = this.textTertiary,
        textDisabled: Color = this.textDisabled,
        textInverted: Color = this.textInverted,
        textWhite: Color = this.textWhite,
        textBrand: Color = this.textBrand,
        surfaceBackground: Color = this.surfaceBackground,
        surfaceBackground2: Color = this.surfaceBackground2,
        surfaceBackground3: Color = this.surfaceBackground3,
        surfaceFieldTags: Color = this.surfaceFieldTags,
        surfaceDisabled: Color = this.surfaceDisabled,
        surfaceInverted: Color = this.surfaceInverted,
        surfaceBrand: Color = this.surfaceBrand,
        surfaceBrand1: Color = this.surfaceBrand1,
        surfaceBrand2: Color = this.surfaceBrand2,
        surfaceBrand3: Color = this.surfaceBrand3,
        borderDefault: Color = this.borderDefault,
        borderDisabled: Color = this.borderDisabled,
        borderSecondary: Color = this.borderSecondary,
        borderBrand: Color = this.borderBrand,
        borderInverted: Color = this.borderInverted,
        iconDefault: Color = this.iconDefault,
        iconSecondary: Color = this.iconSecondary,
        iconTertiary: Color = this.iconTertiary,
        iconDisabled: Color = this.iconDisabled,
        iconInverted: Color = this.iconInverted,
        iconWhite: Color = this.iconWhite,
        iconBrand: Color = this.iconBrand,
        complementaryDefault: Color = this.complementaryDefault,
        complementaryShade1: Color = this.complementaryShade1,
        complementaryShade2: Color = this.complementaryShade2,
        complementaryShade3: Color = this.complementaryShade3,
        semanticSuccess: Color = this.semanticSuccess,
        semanticShade: Color = this.semanticShade,
        semanticInfoDefault: Color = this.semanticInfoDefault,
        semanticInfoShade: Color = this.semanticInfoShade,
        semanticWarningDefault: Color = this.semanticWarningDefault,
        semanticWarningShade: Color = this.semanticWarningShade,
        semanticErrorDefault: Color = this.semanticErrorDefault,
        semanticErrorShade: Color = this.semanticErrorShade,
        semanticSuccessShade: Color = this.semanticSuccessShade
    ): BaseplateColors = BaseplateColors(
        primaryDefault = primaryDefault,
        primaryShade1 = primaryShade1,
        primaryShade2 = primaryShade2,
        primaryShade3 = primaryShade3,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textTertiary = textTertiary,
        textDisabled = textDisabled,
        textInverted = textInverted,
        textWhite = textWhite,
        textBrand = textBrand,
        surfaceBackground = surfaceBackground,
        surfaceBackground2 = surfaceBackground2,
        surfaceBackground3 = surfaceBackground3,
        surfaceFieldTags = surfaceFieldTags,
        surfaceDisabled = surfaceDisabled,
        surfaceInverted = surfaceInverted,
        surfaceBrand = surfaceBrand,
        surfaceBrand1 = surfaceBrand1,
        surfaceBrand2 = surfaceBrand2,
        surfaceBrand3 = surfaceBrand3,
        borderDefault = borderDefault,
        borderDisabled = borderDisabled,
        borderSecondary = borderSecondary,
        borderBrand = borderBrand,
        borderInverted = borderInverted,
        iconDefault = iconDefault,
        iconSecondary = iconSecondary,
        iconTertiary = iconTertiary,
        iconDisabled = iconDisabled,
        iconInverted = iconInverted,
        iconWhite = iconWhite,
        iconBrand = iconBrand,
        complementaryDefault = complementaryDefault,
        complementaryShade1 = complementaryShade1,
        complementaryShade2 = complementaryShade2,
        complementaryShade3 = complementaryShade3,
        semanticSuccess = semanticSuccess,
        semanticShade = semanticShade,
        semanticInfoDefault = semanticInfoDefault,
        semanticInfoShade = semanticInfoShade,
        semanticWarningDefault = semanticWarningDefault,
        semanticWarningShade = semanticWarningShade,
        semanticErrorDefault = semanticErrorDefault,
        semanticErrorShade = semanticErrorShade,
        semanticSuccessShade = semanticSuccessShade
    )

    override fun toString(): String = "BaseplateColors(" +
        "primaryDefault=$primaryDefault, " +
        "primaryShade1=$primaryShade1, " +
        "primaryShade2=$primaryShade2, " +
        "primaryShade3=$primaryShade3, " +
        "textPrimary=$textPrimary, " +
        "textSecondary=$textSecondary," +
        "textTertiary=$textTertiary," +
        "textDisabled=$textDisabled," +
        "textInverted=$textInverted," +
        "textWhite=$textWhite," +
        "textBrand=$textBrand," +
        "surfaceBackground=$surfaceBackground," +
        "surfaceBackground2=$surfaceBackground2," +
        "surfaceBackground3=$surfaceBackground3," +
        "surfaceFieldTags=$surfaceFieldTags," +
        "surfaceDisabled=$surfaceDisabled," +
        "surfaceInverted=$surfaceInverted," +
        "surfaceBrand=$surfaceBrand," +
        "surfaceBrand1=$surfaceBrand1," +
        "surfaceBrand2=$surfaceBrand2," +
        "surfaceBrand3=$surfaceBrand3," +
        "borderDefault=$borderDefault," +
        "borderDisabled=$borderDisabled," +
        "borderSecondary=$borderSecondary," +
        "borderBrand=$borderBrand," +
        "borderInverted=$borderInverted," +
        "iconDefault=$iconDefault," +
        "iconSecondary=$iconSecondary," +
        "iconTertiary=$iconTertiary," +
        "iconDisabled=$iconDisabled," +
        "iconInverted=$iconInverted," +
        "iconWhite=$iconWhite," +
        "iconBrand=$iconBrand," +
        "complementaryDefault=$complementaryDefault," +
        "complementaryShade1=$complementaryShade1," +
        "complementaryShade2=$complementaryShade2," +
        "complementaryShade3=$complementaryShade3," +
        "semanticSuccess=$semanticSuccess," +
        "semanticShade=$semanticShade," +
        "semanticInfoDefault=$semanticInfoDefault," +
        "semanticInfoShade=$semanticInfoShade," +
        "semanticWarningDefault=$semanticWarningDefault," +
        "semanticWarningShade=$semanticWarningShade," +
        "semanticErrorDefault=$semanticErrorDefault," +
        "semanticErrorShade=$semanticErrorShade," +
        ")"
}

// Just change the colors below if your app supports a dark theme. Currently, these colors are for the light theme.
fun darkBaseplateTheme(
    primaryDefault: Color = Color(0xFF004AD9),
    primaryShade1: Color = Color(0xFF4D80EA),
    primaryShade2: Color = Color(0xFFE5EDFB),
    primaryShade3: Color = Color(0xFFF2F6FD),
    textPrimary: Color = Color(0xFF050529),
    textSecondary: Color = Color(0xFF373754),
    textTertiary: Color = Color(0xFF828294),
    textDisabled: Color = Color(0xFFB4B4BF),
    textInverted: Color = Color(0xFF44445F),
    textWhite: Color = Color(0xFFFFFFFF),
    textBrand: Color = Color(0xFF004AD9),
    surfaceBackground: Color = Color(0xFFFFFFFF),
    surfaceBackground2: Color = Color(0xFFF7F7F9),
    surfaceBackground3: Color = Color(0xFFE6E6EA),
    surfaceFieldTags: Color = Color(0xFFF2F2F4),
    surfaceDisabled: Color = Color(0xFFB4B4BF),
    surfaceInverted: Color = Color(0xFF050529),
    surfaceBrand: Color = Color(0xFF004AD9),
    surfaceBrand1: Color = Color(0xFF4D80E4),
    surfaceBrand2: Color = Color(0xFFE5EDFB),
    surfaceBrand3: Color = Color(0xFFF2F6FD),
    borderDefault: Color = Color(0xFFE6E6EA),
    borderDisabled: Color = Color(0xFFB4B4BF),
    borderSecondary: Color = Color(0xFF44445F),
    borderBrand: Color = Color(0xFF004AD9),
    borderInverted: Color = Color(0xFFFFFFFF),
    iconDefault: Color = Color(0xFF050529),
    iconSecondary: Color = Color(0xFF44445F),
    iconTertiary: Color = Color(0xFF828294),
    iconDisabled: Color = Color(0xFFB4B4BF),
    iconInverted: Color = Color(0xFFFFFFFF),
    iconWhite: Color = Color(0xFFFFFFFF),
    iconBrand: Color = Color(0xFF004AD9),
    complementaryDefault: Color = Color(0xFF53CE57),
    complementaryShade1: Color = Color(0xFF87DD89),
    complementaryShade2: Color = Color(0xFFEEFAEE),
    complementaryShade3: Color = Color(0xFFF6FDF7),
    semanticSuccess: Color = Color(0xFF458C03),
    semanticSuccessShade: Color = Color(0xFFF9FFF3),
    semanticShade: Color = Color(0xFFECF3E6),
    semanticInfoDefault: Color = Color(0xFF00B2F4),
    semanticInfoShade: Color = Color(0xFFE5F7FE),
    semanticWarningDefault: Color = Color(0xFFFFA722),
    semanticWarningShade: Color = Color(0xFFFFF6E9),
    semanticErrorDefault: Color = Color(0xFFF10021),
    semanticErrorShade: Color = Color(0xFFFFF4F6)
): BaseplateColors = BaseplateColors(
    primaryDefault = primaryDefault,
    primaryShade1 = primaryShade1,
    primaryShade2 = primaryShade2,
    primaryShade3 = primaryShade3,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textTertiary = textTertiary,
    textDisabled = textDisabled,
    textInverted = textInverted,
    textWhite = textWhite,
    textBrand = textBrand,
    surfaceBackground = surfaceBackground,
    surfaceBackground2 = surfaceBackground2,
    surfaceBackground3 = surfaceBackground3,
    surfaceFieldTags = surfaceFieldTags,
    surfaceDisabled = surfaceDisabled,
    surfaceInverted = surfaceInverted,
    surfaceBrand = surfaceBrand,
    surfaceBrand1 = surfaceBrand1,
    surfaceBrand2 = surfaceBrand2,
    surfaceBrand3 = surfaceBrand3,
    borderDefault = borderDefault,
    borderDisabled = borderDisabled,
    borderSecondary = borderSecondary,
    borderBrand = borderBrand,
    borderInverted = borderInverted,
    iconDefault = iconDefault,
    iconSecondary = iconSecondary,
    iconTertiary = iconTertiary,
    iconDisabled = iconDisabled,
    iconInverted = iconInverted,
    iconWhite = iconWhite,
    iconBrand = iconBrand,
    complementaryDefault = complementaryDefault,
    complementaryShade1 = complementaryShade1,
    complementaryShade2 = complementaryShade2,
    complementaryShade3 = complementaryShade3,
    semanticSuccess = semanticSuccess,
    semanticShade = semanticShade,
    semanticInfoDefault = semanticInfoDefault,
    semanticInfoShade = semanticInfoShade,
    semanticWarningDefault = semanticWarningDefault,
    semanticWarningShade = semanticWarningShade,
    semanticErrorDefault = semanticErrorDefault,
    semanticErrorShade = semanticErrorShade,
    semanticSuccessShade = semanticSuccessShade,
)

fun lightBaseplateTheme(
    primaryDefault: Color = Color(0xFF004AD9),
    primaryShade1: Color = Color(0xFF4D80EA),
    primaryShade2: Color = Color(0xFFE5EDFB),
    primaryShade3: Color = Color(0xFFF2F6FD),
    textPrimary: Color = Color(0xFF050529),
    textSecondary: Color = Color(0xFF373754),
    textTertiary: Color = Color(0xFF828294),
    textDisabled: Color = Color(0xFFB4B4BF),
    textInverted: Color = Color(0xFFFFFFFF),
    textWhite: Color = Color(0xFFFFFFFF),
    textBrand: Color = Color(0xFF004AD9),
    surfaceBackground: Color = Color(0xFFFFFFFF),
    surfaceBackground2: Color = Color(0xFFF7F7F9),
    surfaceBackground3: Color = Color(0xFFE6E6EA),
    surfaceFieldTags: Color = Color(0xFFF2F2F4),
    surfaceDisabled: Color = Color(0xFFB4B4BF),
    surfaceInverted: Color = Color(0xFF050529),
    surfaceBrand: Color = Color(0xFF004AD9),
    surfaceBrand1: Color = Color(0xFF4D80E4),
    surfaceBrand2: Color = Color(0xFFE5EDFB),
    surfaceBrand3: Color = Color(0xFFF2F6FD),
    borderDefault: Color = Color(0xFFE6E6EA),
    borderDisabled: Color = Color(0xFFB4B4BF),
    borderSecondary: Color = Color(0xFF44445F),
    borderBrand: Color = Color(0xFF004AD9),
    borderInverted: Color = Color(0xFFFFFFFF),
    iconDefault: Color = Color(0xFF050529),
    iconSecondary: Color = Color(0xFF44445F),
    iconTertiary: Color = Color(0xFF828294),
    iconDisabled: Color = Color(0xFFB4B4BF),
    iconInverted: Color = Color(0xFFFFFFFF),
    iconWhite: Color = Color(0xFFFFFFFF),
    iconBrand: Color = Color(0xFF004AD9),
    complementaryDefault: Color = Color(0xFF53CE57),
    complementaryShade1: Color = Color(0xFF87DD89),
    complementaryShade2: Color = Color(0xFFEEFAEE),
    complementaryShade3: Color = Color(0xFFF6FDF7),
    semanticSuccess: Color = Color(0xFF458C03),
    semanticShade: Color = Color(0xFFECF3E6),
    semanticInfoDefault: Color = Color(0xFF00B2F4),
    semanticInfoShade: Color = Color(0xFFE5F7FE),
    semanticWarningDefault: Color = Color(0xFFFFA722),
    semanticWarningShade: Color = Color(0xFFFFF6E9),
    semanticErrorDefault: Color = Color(0xFFF10021),
    semanticErrorShade: Color = Color(0xFFFFF4F6),
    semanticSuccessShade: Color = Color(0xFFF9FFF3),
): BaseplateColors = BaseplateColors(
    primaryDefault = primaryDefault,
    primaryShade1 = primaryShade1,
    primaryShade2 = primaryShade2,
    primaryShade3 = primaryShade3,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textTertiary = textTertiary,
    textDisabled = textDisabled,
    textInverted = textInverted,
    textWhite = textWhite,
    textBrand = textBrand,
    surfaceBackground = surfaceBackground,
    surfaceBackground2 = surfaceBackground2,
    surfaceBackground3 = surfaceBackground3,
    surfaceFieldTags = surfaceFieldTags,
    surfaceDisabled = surfaceDisabled,
    surfaceInverted = surfaceInverted,
    surfaceBrand = surfaceBrand,
    surfaceBrand1 = surfaceBrand1,
    surfaceBrand2 = surfaceBrand2,
    surfaceBrand3 = surfaceBrand3,
    borderDefault = borderDefault,
    borderDisabled = borderDisabled,
    borderSecondary = borderSecondary,
    borderBrand = borderBrand,
    borderInverted = borderInverted,
    iconDefault = iconDefault,
    iconSecondary = iconSecondary,
    iconTertiary = iconTertiary,
    iconDisabled = iconDisabled,
    iconInverted = iconInverted,
    iconWhite = iconWhite,
    iconBrand = iconBrand,
    complementaryDefault = complementaryDefault,
    complementaryShade1 = complementaryShade1,
    complementaryShade2 = complementaryShade2,
    complementaryShade3 = complementaryShade3,
    semanticSuccess = semanticSuccess,
    semanticShade = semanticShade,
    semanticInfoDefault = semanticInfoDefault,
    semanticInfoShade = semanticInfoShade,
    semanticWarningDefault = semanticWarningDefault,
    semanticWarningShade = semanticWarningShade,
    semanticErrorDefault = semanticErrorDefault,
    semanticErrorShade = semanticErrorShade,
    semanticSuccessShade = semanticSuccessShade,
)

internal fun BaseplateColors.updateColorsFrom(other: BaseplateColors) {
    primaryDefault = other.primaryDefault
    primaryShade1 = other.primaryShade1
    primaryShade2 = other.primaryShade2
    primaryShade3 = other.primaryShade3
    textPrimary = other.textPrimary
    textSecondary = other.textSecondary
    textTertiary = other.textTertiary
    textDisabled = other.textDisabled
    textInverted = other.textInverted
    textWhite = other.textWhite
    textBrand = other.textBrand
    surfaceBackground = other.surfaceBackground
    surfaceBackground2 = other.surfaceBackground2
    surfaceBackground3 = other.surfaceBackground3
    surfaceFieldTags = other.surfaceFieldTags
    surfaceDisabled = other.surfaceDisabled
    surfaceInverted = other.surfaceInverted
    surfaceBrand = other.surfaceBrand
    surfaceBrand1 = other.surfaceBrand1
    surfaceBrand2 = other.surfaceBrand2
    surfaceBrand3 = other.surfaceBrand3
    borderDefault = other.borderDefault
    borderDisabled = other.borderDisabled
    borderSecondary = other.borderSecondary
    borderBrand = other.borderBrand
    borderInverted = other.borderInverted
    iconDefault = other.iconDefault
    iconSecondary = other.iconSecondary
    iconTertiary = other.iconTertiary
    iconDisabled = other.iconDisabled
    iconInverted = other.iconInverted
    iconWhite = other.iconWhite
    iconBrand = other.iconBrand
    complementaryDefault = other.complementaryDefault
    complementaryShade1 = other.complementaryShade1
    complementaryShade2 = other.complementaryShade2
    complementaryShade3 = other.complementaryShade3
    semanticSuccess = other.semanticSuccess
    semanticShade = other.semanticShade
    semanticInfoDefault = other.semanticInfoDefault
    semanticInfoShade = other.semanticInfoShade
    semanticWarningDefault = other.semanticWarningDefault
    semanticWarningShade = other.semanticWarningShade
    semanticErrorDefault = other.semanticErrorDefault
    semanticErrorShade = other.semanticErrorShade
}

internal val LocalBaseplateColors = staticCompositionLocalOf { lightBaseplateTheme() }
