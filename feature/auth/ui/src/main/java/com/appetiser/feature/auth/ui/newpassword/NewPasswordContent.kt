package com.appetiser.feature.auth.ui.newpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.appetiser.core.common.android.util.PasswordRulesUtil
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateTextField
import com.appetiser.core.common.compose.components.utils.SpacedPasswordVisualTransformation
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.auth.ui.R

@Composable
fun NewPasswordContent(
    title: String,
    description: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    buttonText: String,
    onPasswordSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    email: String = ""
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            text = title,
            color = BaseplateTheme.colors.textPrimary,
            style = BaseplateTheme.typography.title1
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        Text(
            text = description,
            color = BaseplateTheme.colors.textSecondary,
            style = BaseplateTheme.typography.bodyRegular
        )
        if (email.isNotBlank()) {
            Text(
                text = email,
                color = BaseplateTheme.colors.textSecondary,
                style = BaseplateTheme.typography.bodySemiBold
            )
        }
        Spacer(modifier = Modifier.height(Paddings.X_LARGE))
        ProfilePasswordField(
            password = password,
            onPasswordChange = onPasswordChange,
            isError = false
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        PasswordCriteria(
            password = password,
            modifier = Modifier.weight(1.0F)
        )
        BaseplateButton(
            text = buttonText,
            onClick = onPasswordSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = PasswordRulesUtil.validatePassword(password, context)
        )
    }
}

@Composable
fun ProfilePasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    var isHidden by rememberSaveable { mutableStateOf(true) }

    BaseplateTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(
                text = stringResource(R.string.password),
                style = BaseplateTheme.typography.subHeadRegular
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    isHidden = isHidden.not()
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (isHidden) {
                            R.drawable.ic_show_password
                        } else {
                            R.drawable.ic_hide_password
                        }
                    ),
                    contentDescription = stringResource(
                        if (isHidden) {
                            R.string.show_your_password
                        } else {
                            R.string.hide_your_password
                        }
                    ),
                    tint = BaseplateTheme.colors.iconDefault
                )
            }
        },
        isError = isError,
        visualTransformation = if (isHidden) {
            SpacedPasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        singleLine = true,
        unfocusedLabelColor = BaseplateTheme.colors.textSecondary
    )
}

@Composable
fun PasswordCriteria(
    password: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val criteria = remember(context) {
        PasswordRulesUtil.getPasswordRules(context)
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.password_must_contain),
            color = BaseplateTheme.colors.textTertiary,
            style = BaseplateTheme.typography.captionBold
        )
        Spacer(modifier = Modifier.height(Paddings.SMALL))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Paddings.SMALL)
        ) {
            items(items = criteria) { (criterion, hasComplied) ->
                PasswordCriterionItem(
                    criterion = criterion,
                    hasComplied = hasComplied(password)
                )
            }
        }
    }
}

@Composable
fun PasswordCriterionItem(
    criterion: String,
    hasComplied: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                if (hasComplied) {
                    R.drawable.ic_check
                } else {
                    R.drawable.ic_button
                }
            ),
            contentDescription = null,
            tint = if (hasComplied) {
                BaseplateTheme.colors.semanticSuccess
            } else {
                BaseplateTheme.colors.iconSecondary
            }
        )
        Spacer(modifier = Modifier.width(Paddings.SMALL))
        Text(
            text = criterion,
            color = if (hasComplied) {
                BaseplateTheme.colors.semanticSuccess
            } else {
                BaseplateTheme.colors.iconSecondary
            },
            style = BaseplateTheme.typography.footnoteRegular
        )
    }
}
