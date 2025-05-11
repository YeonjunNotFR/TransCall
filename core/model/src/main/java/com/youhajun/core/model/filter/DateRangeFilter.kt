package com.youhajun.core.model.filter

import java.time.Instant

data class DateRangeFilter(
    val from: String,
    val to: String
) {
    companion object {
        fun isoFormatRange(from: Instant, to: Instant): DateRangeFilter =
            DateRangeFilter(from.toString(), to.toString())
    }
}