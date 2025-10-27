package com.youhajun.data.common.dto.calling.payload

import com.youhajun.core.model.calling.payload.CameraEnableChanged
import com.youhajun.core.model.calling.payload.MediaStateRequest
import com.youhajun.core.model.calling.payload.MicEnableChanged
import com.youhajun.data.common.dto.calling.payload.CameraEnableChangedDto.Companion.CAMERA_ENABLE_CHANGED
import com.youhajun.data.common.dto.calling.payload.MicEnableChangedDto.Companion.MIC_ENABLE_CHANGED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface MediaStateRequestDto : RequestPayloadDto

@Serializable
@SerialName(CAMERA_ENABLE_CHANGED)
data class CameraEnableChangedDto(
    @SerialName("isEnabled")
    val isEnabled: Boolean,
) : MediaStateRequestDto {
    companion object {
        const val CAMERA_ENABLE_CHANGED: String = "cameraEnableChanged"
    }
}

@Serializable
@SerialName(MIC_ENABLE_CHANGED)
data class MicEnableChangedDto(
    @SerialName("isEnabled")
    val isEnabled: Boolean,
) : MediaStateRequestDto {
    companion object {
        const val MIC_ENABLE_CHANGED: String = "micEnableChanged"
    }
}

fun MediaStateRequest.toDto(): MediaStateRequestDto = when (this) {
    is CameraEnableChanged -> CameraEnableChangedDto(
        isEnabled = isEnabled
    )

    is MicEnableChanged -> MicEnableChangedDto(
        isEnabled = isEnabled
    )
}