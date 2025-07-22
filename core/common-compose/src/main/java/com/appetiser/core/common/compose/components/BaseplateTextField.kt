package com.appetiser.core.common.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun BaseplateTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textStyle: TextStyle = BaseplateTheme.typography.bodyRegular,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    contentHeight: Dp = Dp.Unspecified,
    backgroundColor: Color = BaseplateTheme.colors.surfaceBackground,
    activeBackgroundColor: Color = BaseplateTheme.colors.primaryShade2,
    activeStrokeColor: Color = BaseplateTheme.colors.primaryDefault,
    defaultStrokeColor: Color = BaseplateTheme.colors.borderDefault,
    focusedTextColor: Color = BaseplateTheme.colors.textPrimary,
    unfocusedTextColor: Color = BaseplateTheme.colors.textPrimary,
    unfocusedLabelColor: Color = BaseplateTheme.colors.borderDisabled,
    focusedLabelColor: Color = BaseplateTheme.colors.primaryDefault,
    disabledContainerColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    errorStrokeColor: Color = BaseplateTheme.colors.semanticErrorDefault,
    errorContainerColor: Color = BaseplateTheme.colors.semanticErrorShade,
    errorLabelColor: Color = BaseplateTheme.colors.semanticErrorDefault,
    cursorColor: Color = BaseplateTheme.colors.primaryDefault,
    maxLength: Int? = null,
    showMaxCounter: Boolean = false
) {
    val isFocused by interactionSource.collectIsFocusedAsState()

    val resolvedStrokeColor by animateColorAsState(
        targetValue =
        when {
            isError -> errorStrokeColor
            isFocused -> activeStrokeColor
            else -> defaultStrokeColor
        },
        label = "Background Color"
    )
    Box(modifier = modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight)
                .border(
                    BorderStroke(1.dp, resolvedStrokeColor),
                    shape = shape
                ),
            value = value,
            onValueChange = { newText ->
                if (maxLength == null || newText.length <= maxLength) {
                    onValueChange(newText)
                } else if (newText.length < value.length) {
                    onValueChange(newText)
                }
            },
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = activeBackgroundColor,
                unfocusedContainerColor = backgroundColor,
                errorContainerColor = errorContainerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = focusedTextColor,
                unfocusedTextColor = unfocusedTextColor,
                focusedLabelColor = focusedLabelColor,
                unfocusedLabelColor = unfocusedLabelColor,
                errorLabelColor = errorLabelColor,
                disabledContainerColor = disabledContainerColor,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = cursorColor,
            ),
            shape = shape
        )
        if (maxLength != null && showMaxCounter) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = Paddings.LARGE, bottom = Paddings.SMALL) // Add padding
                    .zIndex(1f)
            ) {
                Text(
                    text = "${value.length} / $maxLength",
                    style = BaseplateTheme.typography.footnoteRegular.copy(
                        color = BaseplateTheme.colors.textSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun BaseplateTextFieldValue(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textStyle: TextStyle = BaseplateTheme.typography.bodyRegular,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(Radius.SMALL),
    backgroundColor: Color = BaseplateTheme.colors.surfaceBackground,
    activeBackgroundColor: Color = BaseplateTheme.colors.primaryShade2,
    activeStrokeColor: Color = BaseplateTheme.colors.primaryDefault,
    defaultStrokeColor: Color = BaseplateTheme.colors.borderDefault,
    focusedTextColor: Color = BaseplateTheme.colors.textPrimary,
    unfocusedTextColor: Color = BaseplateTheme.colors.textPrimary,
    unfocusedLabelColor: Color = BaseplateTheme.colors.borderDisabled,
    focusedLabelColor: Color = BaseplateTheme.colors.primaryDefault,
    disabledContainerColor: Color = BaseplateTheme.colors.surfaceFieldTags,
    cursorColor: Color = BaseplateTheme.colors.primaryDefault,
    maxLength: Int? = null,
    showMaxCounter: Boolean = false
) {
    val isFocused by interactionSource.collectIsFocusedAsState()

    val resolvedStrokeColor by animateColorAsState(
        targetValue = if (isFocused) activeStrokeColor else defaultStrokeColor,
        label = "Background Color"
    )
    Box(modifier = modifier.fillMaxWidth()) {
        TextField(
            modifier = modifier
                .border(
                    BorderStroke(1.dp, resolvedStrokeColor),
                    shape = shape
                ),
            value = value,
            onValueChange = { newText ->
                if (maxLength == null || newText.text.length <= maxLength) {
                    onValueChange(newText)
                } else if (newText.text.length < value.text.length) {
                    onValueChange(newText)
                }
            },
            enabled = enabled,
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = activeBackgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = focusedTextColor,
                unfocusedTextColor = unfocusedTextColor,
                unfocusedLabelColor = unfocusedLabelColor,
                focusedLabelColor = focusedLabelColor,
                disabledContainerColor = disabledContainerColor,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = cursorColor,
            ),
            shape = shape,
        )
        if (maxLength != null && showMaxCounter) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = Paddings.LARGE, bottom = Paddings.SMALL) // Add padding
                    .zIndex(1f)
            ) {
                Text(
                    text = "${value.text.length} / $maxLength",
                    style = BaseplateTheme.typography.footnoteRegular.copy(
                        color = BaseplateTheme.colors.textSecondary
                    )
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun BaseplateTextFieldPreview() {
    val (value, setValue) = remember { mutableStateOf("") }
    BaseplateTheme {
        BaseplateTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = setValue,
            label = { Text(text = "Input") },
        )
    }
}
