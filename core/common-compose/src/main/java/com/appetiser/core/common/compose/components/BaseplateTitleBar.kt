package com.appetiser.core.common.compose.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.ui.BaseplateTheme

@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable (RowScope.() -> Unit)? = null,
    // TODO We should not use textstyle: We can set sizes (Small, Medium, Large) to limit option for topbar text sizes.
    style: TextStyle = BaseplateTheme.typography.title1,
) {
    Row(
        modifier = modifier.padding(top = 10.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f), text = title, style = style
        )
        actions?.invoke(this)
    }
}

@Preview(showBackground = true)
@Composable
private fun TitleBarPreview() {
    TitleBar(title = "Title")
}

