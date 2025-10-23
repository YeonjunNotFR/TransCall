package com.youhajun.transcall.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youhajun.core.design.R

enum class RelativeTimePatterns {
    AUTO_HOUR_MIN,
    HOUR_MIN,
    DAY_HOUR_MIN,
    MIN_ONLY;

    fun format(
        createdAtSec: Long,
        nowSec: Long = System.currentTimeMillis() / 1000,
        dayLabel: String,
        hourLabel: String,
        minLabel: String,
        agoLabel: String,
        justNowLabel: String,
    ): String {
        val diffSec = (nowSec - createdAtSec).coerceAtLeast(0).toInt()
        val totalMin = diffSec / 60
        val totalHour = totalMin / 60
        val days = totalHour / 24
        val hoursR = totalHour % 24
        val minutesR = totalMin % 60

        fun minOnly(): String = "${totalMin}${minLabel} $agoLabel"

        fun hourMin(): String = if (minutesR == 0) {
            "${totalHour}${hourLabel} $agoLabel"
        } else {
            "${totalHour}${hourLabel} ${minutesR}${minLabel} $agoLabel"
        }

        fun dayHourMin(): String {
            val parts = buildList {
                if (days > 0) add("${days}${dayLabel}")
                if (hoursR > 0) add("${hoursR}${hourLabel}")
                if (minutesR > 0) add("${minutesR}${minLabel}")
            }
            return if (parts.isEmpty()) justNowLabel else parts.joinToString(" ") + " $agoLabel"
        }

        if (totalMin < 1) return justNowLabel

        return when (this) {
            AUTO_HOUR_MIN -> if (totalMin < 60) minOnly() else hourMin()
            HOUR_MIN -> hourMin()
            DAY_HOUR_MIN -> dayHourMin()
            MIN_ONLY -> minOnly()
        }
    }
}

@Composable
fun Long.toUiRelativeString(pattern: RelativeTimePatterns): String {
    val day = stringResource(R.string.common_duration_day_label)
    val hour = stringResource(R.string.common_duration_hour_label)
    val min = stringResource(R.string.common_duration_minute_label)
    val ago = stringResource(R.string.common_relative_ago)
    val now = stringResource(R.string.common_relative_now)
    return pattern.format(
        createdAtSec = this,
        dayLabel = day,
        hourLabel = hour,
        minLabel = min,
        agoLabel = ago,
        justNowLabel = now
    )
}