package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.MediaMessageType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface MediaDataDto : CallingDataDto {

    @Serializable
    @SerialName("audioStateChange")
    data class AudioStateChangeDto(
        @SerialName("userId")
        val userId: String,
        @SerialName("isMicEnabled")
        val isMicEnabled: Boolean,
        @SerialName("mediaContentType")
        val mediaContentType: String,
        @SerialName("isSpeaking")
        val isSpeaking: Boolean
    ) : MediaDataDto

    @Serializable
    @SerialName("videoStateChange")
    data class VideoStateChangeDto(
        @SerialName("userId")
        val userId: String,
        @SerialName("mediaContentType")
        val mediaContentType: String,
        @SerialName("isVideoEnabled")
        val isVideoEnabled: Boolean,
    ) : MediaDataDto

    override fun toModel(): MediaMessageType {
        return when (this) {
            is AudioStateChangeDto -> MediaMessageType.AudioStateChange(
                userId = userId,
                isMicEnabled = isMicEnabled,
                mediaContentType = mediaContentType,
                isSpeaking = isSpeaking
            )
            is VideoStateChangeDto -> MediaMessageType.VideoStateChange(
                userId = userId,
                mediaContentType = mediaContentType,
                isVideoEnabled = isVideoEnabled
            )
        }
    }
}

internal fun MediaMessageType.toDto(): MediaDataDto {
    return when (this) {
        is MediaMessageType.AudioStateChange -> MediaDataDto.AudioStateChangeDto(
            userId = userId,
            isMicEnabled = isMicEnabled,
            mediaContentType = mediaContentType,
            isSpeaking = isSpeaking
        )
        is MediaMessageType.VideoStateChange -> MediaDataDto.VideoStateChangeDto(
            userId = userId,
            mediaContentType = mediaContentType,
            isVideoEnabled = isVideoEnabled
        )
    }
}