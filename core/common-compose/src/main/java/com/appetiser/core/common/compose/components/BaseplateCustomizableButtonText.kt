package com.appetiser.core.common.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.components.utils.ColorUtils
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun BaseplateCustomizableButtonText(
    fieldValue: String,
    label: String,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(Radius.SMALL),
    defaultBackgroundColor: Color = BaseplateTheme.colors.surfaceBackground,
    activeBackgroundColor: Color = BaseplateTheme.colors.primaryShade3,
    activeStrokeColor: Color = BaseplateTheme.colors.primaryDefault,
    defaultStrokeColor: Color = BaseplateTheme.colors.borderDisabled,
    focusedTextColor: Color = BaseplateTheme.colors.primaryDefault,
    descriptionColor: Color = BaseplateTheme.colors.textPrimary,
    unfocusedTextColor: Color = BaseplateTheme.colors.textTertiary,
    isActive: Boolean = false,
    labelStyle: TextStyle = BaseplateTheme.typography.captionRegular,
    descriptionStyle: TextStyle = BaseplateTheme.typography.bodyRegular,
    onFieldValueChanged: (String) -> Unit = {}
) {
    TrackFieldValueChanges(fieldValue, onFieldValueChanged)

    val context = LocalContext.current
    val resolvedStrokeColor = ColorUtils.getResolvedColor(
        context,
        isActive,
        activeStrokeColor,
        defaultStrokeColor,
        R.string.stroke_color
    )

    val resolvedTitleColor = ColorUtils.getResolvedColor(
        context,
        isActive,
        focusedTextColor,
        unfocusedTextColor,
        R.string.title_color
    )

    val resolvedDescriptionColor = ColorUtils.getResolvedColor(
        context,
        isActive,
        descriptionColor,
        unfocusedTextColor,
        R.string.description_color
    )

    val resolvedBackgroundColor = ColorUtils.getResolvedColor(
        context,
        isActive,
        activeBackgroundColor,
        defaultBackgroundColor,
        R.string.background_color
    )

    Box(

        modifier = modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, resolvedStrokeColor),
                shape = shape,
            )
            .background(resolvedBackgroundColor)
            .clickable {
                onClick(if (!isActive) !isActive else isActive)
            }
            .padding(horizontal = Paddings.LARGE, vertical = Paddings.MEDIUM)

    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                style = labelStyle,
                color = resolvedTitleColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = fieldValue,
                modifier = Modifier.fillMaxWidth(),
                color = resolvedDescriptionColor,
                style = descriptionStyle
            )
        }
    }
}

@Composable
private fun TrackFieldValueChanges(
    fieldValue: String,
    onFieldValueChanged: (String) -> Unit
) {
    var previousFieldValue by remember { mutableStateOf(fieldValue) }
    LaunchedEffect(fieldValue) {
        if (fieldValue != previousFieldValue) {
            onFieldValueChanged(fieldValue)
            previousFieldValue = fieldValue
        }
    }
}

@Composable
@Preview
fun BaseplateButtonTextPreview() {
    BaseplateTheme {
        BaseplateCustomizableButtonText(
            modifier = Modifier.fillMaxWidth(),
            fieldValue = "DD/MM/YY",
            label = "Date of birth",
            onClick = {}
        )
    }
}
