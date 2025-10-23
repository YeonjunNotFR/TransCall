package com.youhajun.transcall.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youhajun.core.design.R
import java.util.Locale

enum class TimePatterns {
    CLOCK_HOUR_MIN_SEC,
    CLOCK_HOUR_MIN,
    LABEL_HOUR_MIN_SEC,
    LABEL_HOUR_MIN;

    fun format(
        totalSeconds: Int,
        hourLabel: String,
        minLabel: String,
        secLabel: String,
        locale: Locale = Locale.getDefault()
    ): String {
        val secs = totalSeconds.coerceAtLeast(0)
        val h = secs / 3600
        val m = (secs % 3600) / 60
        val s = secs % 60

        fun clockHms() = String.format(locale, "%02d:%02d:%02d", h, m, s)
        fun clockHm() = String.format(locale, "%02d:%02d", h, m)

        fun labelHms(): String = buildString {
            if (h > 0) append("$h$hourLabel ")
            if (m > 0) append("$m$minLabel ")
            if (s > 0 || (h == 0 && m == 0)) append("$s$secLabel")
        }.trim()

        fun labelHm(): String = buildString {
            if (h > 0) append("$h$hourLabel ")
            if (m > 0 || (h == 0 && s == 0)) append("$m$minLabel")
        }.trim()

        return when (this) {
            CLOCK_HOUR_MIN_SEC -> clockHms()
            CLOCK_HOUR_MIN -> clockHm()
            LABEL_HOUR_MIN_SEC -> labelHms()
            LABEL_HOUR_MIN -> labelHm()
        }
    }
}

@Composable
fun Int.toUiDurationString(pattern: TimePatterns): String {
    val hourLabel = stringResource(R.string.common_duration_hour_label)
    val minLabel = stringResource(R.string.common_duration_minute_label)
    val secLabel = stringResource(R.string.common_duration_second_label)

    return pattern.format(this, hourLabel, minLabel, secLabel)
}