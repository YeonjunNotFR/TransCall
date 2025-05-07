package com.youhajun.core.model

data class RemainTime(
    val remainingSeconds: Int,
    val resetAtEpochSeconds: Long,
    val dailyLimitSeconds: Int? = null
) {
    val remainingMinutes: Int
        get() = remainingSeconds / 60
}