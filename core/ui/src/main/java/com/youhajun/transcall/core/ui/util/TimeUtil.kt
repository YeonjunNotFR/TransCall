package com.youhajun.transcall.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youhajun.core.design.R
import java.util.Locale

enum class TimeFormatPatterns {
    CLOCK_HOUR_MIN_SEC,
    CLOCK_HOUR_MIN,
    LABEL_HOUR_MIN_SEC,
    LABEL_HOUR_MIN;

    fun format(h: Int, m: Int, s: Int, hourLabel: String, minLabel: String, secLabel: String): String {
        val locale = Locale.getDefault()
        return when (this) {
            CLOCK_HOUR_MIN_SEC -> String.format(locale,"%02d:%02d:%02d", h, m, s)
            CLOCK_HOUR_MIN -> String.format(locale,"%02d:%02d", h, m)
            LABEL_HOUR_MIN_SEC -> buildString {
                if (h > 0) append("$h$hourLabel ")
                if (m > 0) append("$m$minLabel ")
                if (s > 0) append("$s$secLabel")
            }.trim()
            LABEL_HOUR_MIN -> buildString {
                if (h > 0) append("$h$hourLabel ")
                if (m > 0) append("$m$minLabel")
            }.trim()
        }
    }
}

@Composable
fun Int.toUiDurationString(pattern: TimeFormatPatterns): String {
    val hourLabel = stringResource(R.string.common_duration_hour_label)
    val minLabel = stringResource(R.string.common_duration_minute_label)
    val secLabel = stringResource(R.string.common_duration_second_label)

    val h = this / 3600
    val m = (this % 3600) / 60
    val s = this % 60

    return pattern.format(h, m, s, hourLabel, minLabel, secLabel)
}