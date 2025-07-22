package com.appetiser.core.common.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.ui.BaseplateColors
import com.appetiser.core.common.compose.ui.BaseplateTheme
import kotlin.math.ln
import kotlin.math.roundToInt

@Composable
fun BaseplateSwitch(
    isChecked: Boolean,
    isEnabled: Boolean = true,
    onToggle: (Boolean) -> Unit,
    checkedThumbColor: Color = BaseplateTheme.colors.surfaceBackground,
    checkedTrackColor: Color = BaseplateTheme.colors.semanticSuccess,
    uncheckedThumbColor: Color = BaseplateTheme.colors.surfaceBackground,
    uncheckedTrackColor: Color = BaseplateTheme.colors.borderDisabled,
    disabledCheckedThumbColor: Color = BaseplateTheme.colors.surfaceBackground,
    disabledCheckedTrackColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    disabledUncheckedThumbColor: Color = BaseplateTheme.colors.surfaceBackground,
    disabledUncheckedTrackColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    trackWidth: Dp = 51.dp.roundToNearestMultipleOf4(),
    trackHeight: Dp = 31.dp.roundToNearestMultipleOf4(),
    thumbDiameter: Dp = 27.dp.roundToNearestMultipleOf4(),
    trackStrokeWidth: Dp = 31.dp.roundToNearestMultipleOf4(),
    thumbPadding: Dp = 2.dp.roundToNearestMultipleOf4(),
    thumbRippleRadius: Dp = 24.dp.roundToNearestMultipleOf4(),
) {
    CustomSwitch(
        checked = isChecked,
        onCheckedChange = onToggle,
        enabled = isEnabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = checkedThumbColor,
            checkedTrackColor = checkedTrackColor,
            uncheckedThumbColor = uncheckedThumbColor,
            uncheckedTrackColor = uncheckedTrackColor,
            disabledCheckedThumbColor = disabledCheckedThumbColor,
            disabledCheckedTrackColor = disabledCheckedTrackColor,
            disabledUncheckedThumbColor = disabledUncheckedThumbColor,
            disabledUncheckedTrackColor = disabledUncheckedTrackColor,
        ),
        trackStrokeWidth = trackStrokeWidth,
        trackWidth = trackWidth,
        trackHeight = trackHeight,
        thumbDiameter = thumbDiameter,
        thumbRippleRadius = thumbRippleRadius,
        thumbPadding = thumbPadding,
    )
}

private val CustomSwitchImpl: @Composable BoxScope.(
    checked: Boolean,
    enabled: Boolean,
    colors: SwitchColors,
    trackWidth: Dp,
    trackStrokeWidth: Dp,
    interactionSource: InteractionSource,
    thumbRippleRadius: Dp,
    thumbDiameter: Dp,
    thumbPadding: Dp,
    interactions: SnapshotStateList<Interaction>,
) -> Unit =
    {
            checked,
            enabled,
            colors,
            trackWidth,
            trackStrokeWidth,
            interactionSource,
            thumbRippleRadius,
            thumbDiameter,
            thumbPadding,
            _
        ->
        val thumbDefaultElevation = 1.dp
        val thumbPressElevation = 6.dp
        val isPressed by interactionSource.collectIsPressedAsState()
        val elevation = if (isPressed) {
            thumbPressElevation
        } else {
            thumbDefaultElevation
        }
        val trackColor = computeTrackColor(enabled = enabled, checked = checked, switchColors = colors)
        Canvas(
            Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
        ) {
            drawTrack(trackColor, trackWidth.toPx(), trackStrokeWidth.toPx())
        }
        // for moving the thumb
        val alignment by animateAlignmentAsState(if (checked) 1f else -1f)
        val thumbColor = computeThumbColor(enabled = enabled, checked = checked, switchColors = colors)

        val resolvedThumbColor by animateColorAsState(
            resolveThumbColor(
                thumbColor = thumbColor,
                absoluteElevation = 4.dp

            ),
        )

        Spacer(
            Modifier
                .align(alignment)
                .indication(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = false, radius = thumbRippleRadius),
                )
                .padding(thumbPadding)
                .requiredSize(thumbDiameter)
                .shadow(elevation, CircleShape, clip = false)
                .background(resolvedThumbColor, CircleShape),
        )
    }

@Composable
internal fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SwitchColors = SwitchDefaults.colors(),
    trackWidth: Dp,
    trackHeight: Dp,
    trackStrokeWidth: Dp,
    thumbRippleRadius: Dp,
    thumbDiameter: Dp,
    thumbPadding: Dp,
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    },
    interactions: SnapshotStateList<Interaction> = remember { mutableStateListOf<Interaction>() },
) {
    val indication = ripple()
    val toggleableModifier =
        if (onCheckedChange != null) {
            Modifier.toggleable(
                value = checked,
                interactionSource = interactionSource,
                onValueChange = onCheckedChange,
                indication = indication,
                role = Role.Switch,
                enabled = enabled,
            )
        } else {
            Modifier
        }

    Box(
        modifier
            .then(
                if (onCheckedChange != null) {
                    Modifier.minimumInteractiveComponentSize()
                } else {
                    Modifier
                },
            )
            .then(toggleableModifier)
            .wrapContentSize(Alignment.Center)
            .requiredSize(width = trackWidth, height = trackHeight),
    ) {
        CustomSwitchImpl(
            checked,
            enabled,
            colors,
            trackWidth,
            trackStrokeWidth,
            interactionSource,
            thumbRippleRadius,
            thumbDiameter,
            thumbPadding,
            interactions,
        )
    }
}

@Composable
fun Dp.roundToNearestMultipleOf4(): Dp {
    val valueInPixels = with(LocalDensity.current) { toPx() }
    val roundedValueInPixels = (valueInPixels / 4).roundToInt() * 4
    return with(LocalDensity.current) { roundedValueInPixels.toDp() }
}

private fun DrawScope.drawTrack(trackColor: Color, trackWidth: Float, strokeWidth: Float) {
    val strokeRadius = strokeWidth / 2
    drawLine(
        trackColor,
        Offset(strokeRadius, center.y),
        Offset(trackWidth - strokeRadius, center.y),
        strokeWidth,
        StrokeCap.Round,
    )
}

@Composable
private fun computeThumbColor(
    switchColors: SwitchColors,
    enabled: Boolean,
    checked: Boolean,
): Color = when {
    !enabled && checked -> switchColors.disabledCheckedThumbColor
    !enabled -> switchColors.disabledUncheckedThumbColor
    checked -> switchColors.checkedThumbColor
    else -> switchColors.uncheckedThumbColor
}

@Composable
private fun computeTrackColor(
    switchColors: SwitchColors,
    enabled: Boolean,
    checked: Boolean,
): Color = when {
    !enabled && checked -> switchColors.disabledCheckedTrackColor
    !enabled -> switchColors.disabledUncheckedTrackColor
    checked -> switchColors.checkedTrackColor
    else -> switchColors.uncheckedTrackColor
}

@Composable
private fun resolveThumbColor(
    thumbColor: Color,
    absoluteElevation: Dp,
): Color = if (thumbColor == BaseplateTheme.colors.surfaceBackground) {
    BaseplateTheme.colors.surfaceColorAtElevation(absoluteElevation)
} else {
    thumbColor
}

@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float,
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue, label = "")
    return remember {
        derivedStateOf {
            BiasAlignment(horizontalBias = bias, verticalBias = 0f)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleSwitchPreview() {
    BaseplateSwitch(
        isChecked = true,
        onToggle = {},
    )
}

fun BaseplateColors.surfaceColorAtElevation(
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return textDisabled
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return primaryDefault.copy(alpha = alpha).compositeOver(textWhite)
}
