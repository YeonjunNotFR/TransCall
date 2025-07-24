package com.youhajun.core.model

data class RemainTime(
    val remainingSeconds: Long = 0,
    val resetAtEpochSeconds: Long = 0,
) {
    val remainingMinutes: Long
        get() = remainingSeconds / 60
}