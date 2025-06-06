package com.youhajun.core.model.calling

sealed interface MediaMessageType : CallingMessageType {
    data class AudioStateChange(
        val userId: String,
        val isMicEnabled: Boolean,
        val mediaContentType: String,
        val isSpeaking: Boolean
    ) : MediaMessageType

    data class VideoStateChange(
        val userId: String,
        val mediaContentType: String,
        val isVideoEnabled: Boolean,
    ) : MediaMessageType
}
