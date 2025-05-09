package com.youhajun.core.model

data class RemainTime(
    val remainingSeconds: Int = 0,
    val resetAtEpochSeconds: Long = 0,
    val dailyLimitSeconds: Int? = null
) {
    val remainingMinutes: Int
        get() = remainingSeconds / 60
}