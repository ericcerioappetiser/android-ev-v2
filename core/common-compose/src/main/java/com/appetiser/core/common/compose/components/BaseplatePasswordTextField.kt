package com.appetiser.core.common.compose.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.components.utils.SpacedPasswordVisualTransformation
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun BaseplatePasswordTextField(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    password: String,
    trailingIconOn: ImageVector,
    trailingIconOff: ImageVector,
    onPasswordChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions
) {
    var passwordVisible by remember { mutableStateOf(false) }

    BaseplateTextField(
        modifier = modifier,
        singleLine = true,
        label = label,
        value = password,
        onValueChange = onPasswordChange,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            SpacedPasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                val eyeIcon = if (passwordVisible) {
                    trailingIconOff
                } else {
                    trailingIconOn
                }
                Icon(
                    imageVector = eyeIcon,
                    contentDescription = stringResource(R.string.password),
                    tint = BaseplateTheme.colors.iconDefault
                )
            }
        },
        keyboardOptions = keyboardOptions
    )
}
