package com.appetiser.core.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.compose.R
import com.appetiser.core.common.compose.ui.BaseplateTheme

/**
 * A customizable Empty State Composable for Jetpack Compose.
 * Displays an icon, title, subtitle, and an optional button.
 *
 * @param modifier Modifier for the root Column.
 * @param title The main title text for the empty state.
 * @param subtitle The descriptive subtitle text for the empty state.
 * @param iconPainter The painter for the icon to display. If null, a placeholder will be shown.
 * @param buttonText The text for the optional button. If null, no button will be displayed.
 * @param onButtonClick Action to perform when the optional button is clicked. Only relevant if [buttonText] is not null.
 * @param titleStyle Text style for the title.
 * @param descriptionStyle Text style for the subtitle/description.
 * @param iconSize Size of the icon/image.
 * @param descriptionColor Color for the subtitle text.
 */
@Composable
fun CustomEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = painterResource(R.drawable.ic_empty_state),
    titleStyle: TextStyle = BaseplateTheme.typography.title3,
    descriptionStyle: TextStyle = BaseplateTheme.typography.bodyRegular,
    iconSize: Dp = 144.dp,
    descriptionColor: Color = BaseplateTheme.colors.textTertiary,
    buttonSlot: (@Composable () -> Unit)? = null,
    subtitle: String? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (iconPainter != null) {
            Image(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = titleStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = descriptionStyle,
                textAlign = TextAlign.Center,
                color = descriptionColor,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }


        if (buttonSlot != null) {
            Spacer(modifier = Modifier.height(24.dp))
            buttonSlot()
        }
    }
}
