package com.youhajun.data.common.dto.user

import com.youhajun.core.model.RemainTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemainTimeDto(
    @SerialName("remainingSeconds")
    val remainingSeconds: Long = 0,
    @SerialName("resetAtEpochSeconds")
    val resetAtEpochSeconds: Long = 0,
) {
    fun toModel(): RemainTime = RemainTime(
        remainingSeconds = remainingSeconds,
        resetAtEpochSeconds = resetAtEpochSeconds,
    )
}