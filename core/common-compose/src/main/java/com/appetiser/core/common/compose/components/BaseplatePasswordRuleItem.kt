package com.appetiser.core.common.compose.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.appetiser.core.common.compose.dimensions.Paddings

@Composable
fun BaseplatePasswordRuleItem(
    rule: Pair<String, (String) -> Boolean>,
    password: String,
    iconContent: @Composable (Boolean) -> Unit,
    textContent: @Composable (Boolean, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (ruleText, isValid) = rule

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        iconContent(isValid(password))
        Spacer(modifier = Modifier.width(Paddings.SMALL))
        textContent(isValid(password), ruleText)
    }
}
