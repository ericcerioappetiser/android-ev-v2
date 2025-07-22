package com.appetiser.core.common.compose.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.ui.BaseplateTheme
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun BaseplateChronometer(
    duration: Duration,
    modifier: Modifier = Modifier,
    isCountdown: Boolean = true,
    style: TextStyle = LocalTextStyle.current,
    onTimerComplete: () -> Unit
) {
    val currentOnTimerComplete by rememberUpdatedState(onTimerComplete)
    val timer by produceState(
        initialValue = if (isCountdown) {
            duration.minus(1.seconds)
        } else {
            0.seconds
        },
        key1 = duration,
        key2 = isCountdown
    ) {
        if (isCountdown) {
            while (value.isPositive()) {
                delay(1.seconds)
                value = value.minus(1.seconds)
            }
        } else {
            while (value < duration) {
                delay(1.seconds)
                value = value.plus(1.seconds)
            }
        }

        currentOnTimerComplete()
    }

    Text(
        text = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            timer.inWholeSeconds / 60,
            timer.inWholeSeconds % 60
        ),
        modifier = modifier,
        style = style
    )
}

@Preview
@Composable
private fun BaseplateChronometerPreview() {
    BaseplateTheme {
        BaseplateChronometer(
            duration = 1.minutes
        ) {}
    }
}
