package com.appetiser.core.common.compose.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.ui.lightBaseplateTheme

@Composable
fun BaseplateButton(
    modifier: Modifier = Modifier,
    text: String,
    contentPadding: PaddingValues = PaddingValues(Radius.LARGE),
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    backgroundColor: Color = BaseplateTheme.colors.primaryDefault,
    contentColor: Color = BaseplateTheme.colors.textWhite,
    disabledBackgroundColor: Color = BaseplateTheme.colors.surfaceDisabled,
    disabledContentColor: Color = BaseplateTheme.colors.textTertiary,
    enabled: Boolean = true,
    onClick: () -> Unit,
    textStyle: TextStyle = BaseplateTheme.typography.bodyBold
) {
    BaseplateButton(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        contentPadding = contentPadding,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text, style = textStyle)
    }
}

@Composable
fun BaseplateButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BaseplateTheme.colors.primaryDefault,
    contentColor: Color = BaseplateTheme.colors.textInverted,
    disabledBackgroundColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    disabledContentColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    contentPadding: PaddingValues = PaddingValues(Radius.LARGE),
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        shape = shape,
        contentPadding = contentPadding,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledBackgroundColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        onClick = onClick,

    ) {
        content()
    }
}

@Composable
fun BaseplateSecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    contentPadding: PaddingValues = PaddingValues(Radius.LARGE),
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    backgroundColor: Color = BaseplateTheme.colors.primaryShade1,
    contentColor: Color = BaseplateTheme.colors.textInverted,
    disabledBackgroundColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    disabledContentColor: Color = BaseplateTheme.colors.surfaceDisabled,
    enabled: Boolean = true,
    onClick: () -> Unit,
    textStyle: TextStyle = BaseplateTheme.typography.bodyBold
) {
    BaseplateSecondaryButton(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        contentPadding = contentPadding,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text, style = textStyle)
    }
}

@Composable
fun BaseplateSecondaryButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BaseplateTheme.colors.primaryShade1,
    contentColor: Color = BaseplateTheme.colors.primaryShade1,
    disabledBackgroundColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    disabledContentColor: Color = BaseplateTheme.colors.surfaceDisabled,
    contentPadding: PaddingValues = PaddingValues(Radius.LARGE),
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        shape = shape,
        contentPadding = contentPadding,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledBackgroundColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        onClick = onClick,

    ) {
        content()
    }
}

@Composable
fun BaseplateOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    backgroundColor: Color = BaseplateTheme.colors.surfaceBackground,
    contentColor: Color = BaseplateTheme.colors.borderDisabled,
    disabledContentColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    contentPadding: PaddingValues = PaddingValues(Radius.LARGE),
    border: BorderStroke = BorderStroke(
        width = 1.dp,
        color = BaseplateTheme.colors.borderDefault
    ),
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    activeTextColor: Color = BaseplateTheme.colors.textBrand,
    disabledTextColor: Color = BaseplateTheme.colors.textDisabled,
    onClick: () -> Unit,
    textStyle: TextStyle = BaseplateTheme.typography.bodyBold
) {
    val resolvedTextColor by animateColorAsState(
        targetValue = if (enabled) activeTextColor else disabledTextColor,
        label = "Text color"
    )
    BaseplateOutlinedButton(
        modifier = modifier,
        enabled = enabled,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        contentPadding = contentPadding,
        border = border,
        shape = shape,
        onClick = onClick,
    ) {
        Text(text = text, color = resolvedTextColor, style = textStyle)
    }
}

@Composable
fun BaseplateOutlinedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = BaseplateTheme.colors.surfaceBackground,
    contentColor: Color = BaseplateTheme.colors.primaryDefault,
    disabledContentColor: Color = BaseplateTheme.colors.borderDisabled,
    contentPadding: PaddingValues = PaddingValues(Radius.LARGE),
    border: BorderStroke = BorderStroke(
        width = 1.dp,
        color = BaseplateTheme.colors.borderDefault
    ),
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        shape = shape,
        contentPadding = contentPadding,
        enabled = enabled,
        border = border,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        onClick = onClick
    ) {
        content()
    }
}

@Composable
fun BaseplateTextButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = Color.Transparent,
    disabledContentColor: Color = Color.Transparent,
    contentPadding: PaddingValues = PaddingValues(Radius.LARGE),
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    onClick: () -> Unit,
    activeTextColor: Color = BaseplateTheme.colors.primaryDefault,
    disabledTextColor: Color = BaseplateTheme.colors.surfaceDisabled,
    textStyle: TextStyle = BaseplateTheme.typography.bodyBold
) {
    val resolvedTextColor by animateColorAsState(
        targetValue = if (enabled) activeTextColor else disabledTextColor,
        label = "Text color"
    )
    BaseplateTextButton(
        modifier = modifier,
        enabled = enabled,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        contentPadding = contentPadding,
        shape = shape,
        onClick = onClick
    ) {
        Text(text = text, color = resolvedTextColor, style = textStyle)
    }
}

@Composable
fun BaseplateTextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = BaseplateTheme.colors.primaryDefault,
    disabledContentColor: Color = BaseplateTheme.colors.surfaceDisabled,
    contentPadding: PaddingValues = PaddingValues(Radius.LARGE),
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        modifier = modifier,
        shape = shape,
        contentPadding = contentPadding,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        onClick = onClick
    ) {
        content()
    }
}

@Composable
private fun ButtonPreviews(
    enabled: Boolean
) {
    Column {
        @Composable
        fun content() {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(Margins.SMALL))
            Text(text = "Button", style = BaseplateTheme.typography.bodyBold)
        }

        val spacingModifier = Modifier.padding(Paddings.SMALL)

        BaseplateButton(
            modifier = spacingModifier,
            text = "Button",
            enabled = enabled,
            onClick = {}
        )
        BaseplateButton(
            modifier = spacingModifier,
            enabled = enabled,
            onClick = {},
            content = { content() }
        )
        BaseplateSecondaryButton(
            modifier = spacingModifier,
            text = "Button",
            enabled = enabled,
            onClick = {}
        )
        BaseplateSecondaryButton(
            modifier = spacingModifier,
            enabled = enabled,
            onClick = {},
            content = { content() }
        )
        BaseplateOutlinedButton(
            modifier = spacingModifier,
            enabled = enabled,
            text = "Button",
            onClick = {}
        )
        BaseplateOutlinedButton(
            modifier = spacingModifier,
            enabled = enabled,
            onClick = {},
            content = { content() }
        )
        BaseplateTextButton(
            modifier = spacingModifier,
            enabled = enabled,
            text = "Button",
            onClick = {}
        )
        BaseplateTextButton(
            modifier = spacingModifier,
            enabled = enabled,
            onClick = {},
            content = { content() }
        )
    }
}

@Preview(
    showBackground = true,
    name = "Button Enabled - Light Theme",
    group = "Button",
    uiMode = Configuration.UI_MODE_NIGHT_NO // Light Theme
)
@Composable
private fun ButtonEnabledLightThemePreview() {
    BaseplateTheme(colors = lightBaseplateTheme()) {
        ButtonPreviews(enabled = true)
    }
}

@Preview(
    showBackground = true,
    name = "Button Disabled - Light Theme",
    group = "Buttons",
    uiMode = Configuration.UI_MODE_NIGHT_NO // Light Theme
)
@Composable
private fun ButtonDisabledLightThemePreview() {
    BaseplateTheme(colors = lightBaseplateTheme()) {
        ButtonPreviews(enabled = false)
    }
}


