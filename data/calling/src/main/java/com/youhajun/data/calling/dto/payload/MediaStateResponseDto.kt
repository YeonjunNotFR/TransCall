package com.youhajun.data.calling.dto.payload

import com.youhajun.core.model.calling.payload.MediaState
import com.youhajun.core.model.calling.payload.MediaStateChanged
import com.youhajun.core.model.calling.payload.MediaStateInit
import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.data.calling.dto.payload.MediaStateChangedDto.Companion.MEDIA_STATE_CHANGED
import com.youhajun.data.calling.dto.payload.MediaStateInitDto.Companion.MEDIA_STATE_INIT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface MediaStateResponseDto : ResponsePayloadDto

@Serializable
@SerialName(MEDIA_STATE_CHANGED)
internal data class MediaStateChangedDto(
    @SerialName("mediaState")
    val mediaState: MediaStateDto,
) : MediaStateResponseDto {
    companion object {
        internal const val MEDIA_STATE_CHANGED: String = "mediaStateChanged"
    }

    override fun toModel(): ResponsePayload = MediaStateChanged(
        mediaState = mediaState.toModel()
    )
}

@Serializable
@SerialName(MEDIA_STATE_INIT)
internal data class MediaStateInitDto(
    @SerialName("mediaStateList")
    val mediaStateList: List<MediaStateDto>
) : MediaStateResponseDto {
    companion object {
        internal const val MEDIA_STATE_INIT: String = "mediaStateInit"
    }

    override fun toModel(): ResponsePayload = MediaStateInit(
        mediaStateList = mediaStateList.map { it.toModel() }
    )
}

@Serializable
internal data class MediaStateDto(
    @SerialName("userId")
    val userId: String,
    @SerialName("micEnabled")
    val micEnabled: Boolean,
    @SerialName("cameraEnabled")
    val cameraEnabled: Boolean,
) {
    fun toModel(): MediaState = MediaState(
        userId = userId,
        micEnabled = micEnabled,
        cameraEnabled = cameraEnabled,
    )
}