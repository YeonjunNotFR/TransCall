package com.youhajun.transcall.core.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class DateFormatPatterns(val pattern: String) {
    YEAR_MONTH_DAY_HOUR_MINUTE_SECOND("yyyy.MM.dd HH:mm:ss"),
    YEAR_MONTH_DAY_HOUR_MINUTE("yyyy.MM.dd HH:mm"),
    MONTH_DAY_HOUR_MINUTE("MM.dd HH:mm"),
    YEAR_MONTH_DAY("yyyy.MM.dd"),
    HOUR_MINUTE("HH:mm"),
}

fun Long.toUiDateString(format: DateFormatPatterns): String {
    return Instant.ofEpochSecond(this)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern(format.pattern))
}