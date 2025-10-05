package com.youhajun.data.calling.dto.payload

import com.youhajun.core.model.calling.payload.CameraEnableChanged
import com.youhajun.core.model.calling.payload.MediaStateRequest
import com.youhajun.core.model.calling.payload.MicEnableChanged
import com.youhajun.data.calling.dto.payload.CameraEnableChangedDto.Companion.CAMERA_ENABLE_CHANGED
import com.youhajun.data.calling.dto.payload.MicEnableChangedDto.Companion.MIC_ENABLE_CHANGED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface MediaStateRequestDto : RequestPayloadDto

@Serializable
@SerialName(CAMERA_ENABLE_CHANGED)
internal data class CameraEnableChangedDto(
    @SerialName("isEnabled")
    val isEnabled: Boolean,
) : MediaStateRequestDto {
    companion object {
        internal const val CAMERA_ENABLE_CHANGED: String = "cameraEnableChanged"
    }
}

@Serializable
@SerialName(MIC_ENABLE_CHANGED)
internal data class MicEnableChangedDto(
    @SerialName("isEnabled")
    val isEnabled: Boolean,
) : MediaStateRequestDto {
    companion object {
        internal const val MIC_ENABLE_CHANGED: String = "micEnableChanged"
    }
}

internal fun MediaStateRequest.toDto(): MediaStateRequestDto = when (this) {
    is CameraEnableChanged -> CameraEnableChangedDto(
        isEnabled = isEnabled
    )

    is MicEnableChanged -> MicEnableChangedDto(
        isEnabled = isEnabled
    )
}