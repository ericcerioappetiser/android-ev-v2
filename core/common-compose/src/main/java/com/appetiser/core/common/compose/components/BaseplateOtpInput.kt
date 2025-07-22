package com.appetiser.core.common.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.ui.lightBaseplateTheme

const val DEFAULT_BP_OTP_LENGTH = 5

@Composable
fun BaseplateOtpInput(
    modifier: Modifier = Modifier,
    otpLength: Int = DEFAULT_BP_OTP_LENGTH,
    otpValues: List<String> = List(otpLength) { "" },
    isError: Boolean = false,
    textStyle: TextStyle = BaseplateTheme.typography.title2.copy(
        color = BaseplateTheme.colors.textPrimary,
        textAlign = TextAlign.Center,
    ),
    colors: TextFieldColors? = null,
    onOtpChange: (Int, String) -> Unit,
    onOtpComplete: (String) -> Unit,
) {
    val focusRequesters = List(otpLength) { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Margins.SMALL),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        otpValues.forEachIndexed { index, value ->
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(Paddings.SMALL)
                    .focusRequester(focusRequesters[index])
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Backspace) {
                            if (otpValues[index].isEmpty() && index > 0) {
                                onOtpChange(index, "")
                                focusRequesters[index - 1].requestFocus()
                            } else {
                                onOtpChange(index, "")
                            }
                            true
                        } else {
                            false
                        }
                    },
                value = value,
                onValueChange = { newValue ->
                    when {
                        newValue.length == otpLength -> {
                            // Directly set OTP values when pasted
                            val otpDigits = newValue.filter { it.isDigit() }.take(otpLength)
                            otpDigits.forEachIndexed { i, digit ->
                                onOtpChange(i, digit.toString())
                            }
                            keyboardController?.hide()
                        }

                        newValue.length <= 1 -> {
                            // Handle single digit input
                            onOtpChange(index, newValue)
                            val hasNextInput = newValue.isNotEmpty() && index < otpLength - 1
                            val isLastInput = newValue.isNotEmpty() && index == otpLength - 1
                            if (hasNextInput) {
                                focusRequesters[index + 1].requestFocus()
                            } else if (isLastInput) {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        }

                        else -> {
                            // Normally proceed to next input
                            moveFocus(index, otpLength, focusRequesters)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        moveFocus(index, otpLength, focusRequesters)
                    }
                ),
                isError = isError,
                textStyle = textStyle,
                colors = colors ?: OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BaseplateTheme.colors.surfaceBackground,
                    focusedBorderColor = BaseplateTheme.colors.borderBrand,
                    unfocusedContainerColor = BaseplateTheme.colors.surfaceFieldTags,
                    unfocusedBorderColor = BaseplateTheme.colors.borderDefault,
                    errorBorderColor = BaseplateTheme.colors.semanticErrorDefault,
                    errorContainerColor = BaseplateTheme.colors.surfaceBackground
                )
            )

            LaunchedEffect(value) {
                if (otpValues.all { it.isNotEmpty() }) {
                    focusManager.clearFocus()
                    val payload = otpValues.joinToString(separator = "")
                    onOtpComplete.invoke(payload)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }
}

private fun moveFocus(index: Int, otpLength: Int, focusRequesters: List<FocusRequester>) {
    if (index < otpLength - 1) {
        focusRequesters[index + 1].requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun BaseplateOtpInputPreview() {
    val otpValues = remember { mutableStateListOf("", "", "", "", "") }
    BaseplateTheme(colors = lightBaseplateTheme()) {
        BaseplateOtpInput(
            modifier = Modifier,
            otpLength = otpValues.size,
            otpValues = otpValues,
            onOtpChange = { index, value ->
                otpValues[index] = value
            }
        ) {
            // Handle OTP completion
        }
    }
}
