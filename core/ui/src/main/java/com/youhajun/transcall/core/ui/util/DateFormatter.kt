package com.youhajun.transcall.core.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class DateFormatPatterns(val pattern: String) {
    DATE_TIME_WITH_SECONDS("yyyy.MM.dd HH:mm:ss"),
    DATE_TIME_WITHOUT_SECONDS("yyyy.MM.dd HH:mm"),
    DATE_ONLY("yyyy.MM.dd"),
    TIME_ONLY("HH:mm"),
}

fun Long.toUiDateString(format: DateFormatPatterns): String {
    return Instant.ofEpochSecond(this)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern(format.pattern))
}