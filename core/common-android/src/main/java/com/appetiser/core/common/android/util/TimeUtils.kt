package com.appetiser.core.common.android.util

import java.util.Locale
import kotlin.time.Duration.Companion.seconds

object TimeUtils {

    fun Long.formatToMinutesAndSeconds(): String = this
        .seconds
        .toComponents { minutes, remainingSeconds, _ ->
            String.format(
                Locale.getDefault(),
                "%02d:%02d",
                minutes,
                remainingSeconds
            )
        }
}
