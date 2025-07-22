package com.appetiser.core.common.compose.ui


import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.appetiser.core.common.compose.R

/**
 * Baseplate defined typography.
 */

@Immutable
class BaseplateTypography constructor(
    internal val UrbanistRegular: FontFamily = FontFamily(Font(R.font.urbanist_regular)),
    internal val UrbanistMedium: FontFamily = FontFamily(Font(R.font.urbanist_medium)),
    internal val UrbanistSemiBold: FontFamily = FontFamily(Font(R.font.urbanist_semi_bold)),
    internal val UrbanistBold: FontFamily = FontFamily(Font(R.font.urbanist_bold)),
    internal val BeVietnamBold: FontFamily = FontFamily(Font(R.font.be_vietnam_pro_bold)),
    val largeTitle: TextStyle = DefaultTextStyle.merge(
        fontFamily = BeVietnamBold,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeight = 32.sp
    ),
    val title1: TextStyle = DefaultTextStyle.merge(
        fontFamily = BeVietnamBold,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeight = 32.sp
    ),
    val title2: TextStyle = DefaultTextStyle.merge(
        fontFamily = BeVietnamBold,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeight = 24.sp
    ),
    val title3: TextStyle = DefaultTextStyle.merge(
        fontFamily = UrbanistSemiBold,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeight = 24.sp
    ),
    val title3Regular: TextStyle = title3.merge(
        fontFamily = UrbanistRegular,
        fontWeight = FontWeight.Normal,
    ),
    val title3Bold: TextStyle = title3.merge(
        fontWeight = FontWeight.Bold,
        fontFamily = UrbanistBold
    ),
    val bodyBold: TextStyle = DefaultTextStyle.merge(
        fontFamily = UrbanistBold,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
    val bodySemiBold: TextStyle = DefaultTextStyle.merge(
        fontFamily = UrbanistSemiBold,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
    val bodyRegular: TextStyle = DefaultTextStyle.merge(
        fontFamily = UrbanistRegular,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
    val subHeadSemiBold: TextStyle = DefaultTextStyle.merge(
        fontFamily = UrbanistSemiBold,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
    val subHeadRegular: TextStyle = subHeadSemiBold.merge(
        fontFamily = UrbanistRegular,
        fontWeight = FontWeight.Normal
    ),
    val footnoteBold: TextStyle = DefaultTextStyle.merge(
        fontFamily = UrbanistBold,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
    val footnoteRegular: TextStyle = footnoteBold.merge(
        fontFamily = UrbanistRegular,
        fontWeight = FontWeight.Normal
    ),
    val captionBold: TextStyle = DefaultTextStyle.merge(
        fontFamily = UrbanistBold,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
    val captionRegular: TextStyle = captionBold.merge(
        fontFamily = UrbanistRegular,
        fontWeight = FontWeight.Normal
    ),
    val navigationLabel: TextStyle = DefaultTextStyle.merge(
        fontFamily = UrbanistRegular,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight = 12.sp
    ),
) {

    fun copy(
        largeTitle: TextStyle = this.largeTitle,
        title1: TextStyle = this.title1,
        title2: TextStyle = this.title2,
        title3: TextStyle = this.title3,
        bodyBold: TextStyle = this.bodyBold,
        bodySemiBold: TextStyle = this.bodySemiBold,
        bodyRegular: TextStyle = this.bodyRegular,
        subHeadSemiBold: TextStyle = this.subHeadSemiBold,
        subHeadRegular: TextStyle = this.subHeadRegular,
        footnoteBold: TextStyle = this.footnoteBold,
        footnoteRegular: TextStyle = this.footnoteRegular,
        captionBold: TextStyle = this.captionBold,
        captionRegular: TextStyle = this.captionRegular,
        navigationLabel: TextStyle = this.navigationLabel
    ): BaseplateTypography = BaseplateTypography(
        largeTitle = largeTitle,
        title1 = title1,
        title2 = title2,
        title3 = title3,
        bodyBold = bodyBold,
        bodySemiBold = bodySemiBold,
        bodyRegular = bodyRegular,
        subHeadSemiBold = subHeadSemiBold,
        subHeadRegular = subHeadRegular,
        footnoteBold = footnoteBold,
        footnoteRegular = footnoteRegular,
        captionBold = captionBold,
        captionRegular = captionRegular,
        navigationLabel = navigationLabel
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseplateTypography) return false

        if (UrbanistSemiBold != other.UrbanistSemiBold) return false
        if (UrbanistBold != other.UrbanistBold) return false
        if (UrbanistRegular != other.UrbanistRegular) return false
        if (UrbanistSemiBold != other.UrbanistSemiBold) return false
        if (UrbanistMedium != other.UrbanistMedium) return false
        if (largeTitle != other.largeTitle) return false
        if (title1 != other.title1) return false
        if (title2 != other.title2) return false
        if (title3 != other.title3) return false
        if (bodyBold != other.bodyBold) return false
        if (bodySemiBold != other.bodySemiBold) return false
        if (bodyRegular != other.bodyRegular) return false
        if (subHeadSemiBold != other.subHeadSemiBold) return false
        if (subHeadRegular != other.subHeadRegular) return false
        if (footnoteBold != other.footnoteBold) return false
        if (footnoteRegular != other.footnoteRegular) return false
        if (captionBold != other.captionBold) return false
        if (captionRegular != other.captionRegular) return false
        if (navigationLabel != other.navigationLabel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = UrbanistRegular.hashCode()
        result = 31 * result + largeTitle.hashCode()
        result = 31 * result + title1.hashCode()
        result = 31 * result + title2.hashCode()
        result = 31 * result + title3.hashCode()
        result = 31 * result + bodyBold.hashCode()
        result = 31 * result + bodySemiBold.hashCode()
        result = 31 * result + bodyRegular.hashCode()
        result = 31 * result + subHeadSemiBold.hashCode()
        result = 31 * result + subHeadRegular.hashCode()
        result = 31 * result + footnoteBold.hashCode()
        result = 31 * result + footnoteRegular.hashCode()
        result = 31 * result + captionBold.hashCode()
        result = 31 * result + captionRegular.hashCode()
        result = 31 * result + navigationLabel.hashCode()
        return result
    }

    override fun toString(): String =
        "BaseplateTypography(" +
            "largeTitle=$largeTitle, " +
            "title1=$title1, " +
            "title2=$title2, " +
            "title3=$title3, " +
            "bodyBold=$bodyBold, " +
            "bodySemiBold=$bodySemiBold, " +
            "bodyRegular=$bodyRegular, " +
            "subHeadSemiBold=$subHeadSemiBold, " +
            "subHeadRegular=$subHeadRegular, " +
            "footnoteBold=$footnoteBold, " +
            "footnoteRegular=$footnoteRegular, " +
            "captionBold=$captionBold, " +
            "captionRegular=$captionRegular, " +
            "navigationLabel=$navigationLabel, " +
            ")"
}

internal val DefaultTextStyle = TextStyle.Default.copy(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    )
)

internal val LocalBaseplateTypography = staticCompositionLocalOf { BaseplateTypography() }
