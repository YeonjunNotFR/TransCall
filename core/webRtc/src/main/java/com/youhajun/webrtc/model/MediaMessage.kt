package com.youhajun.webrtc.model

sealed interface MediaMessage {
    data class AudioStateChange(
        val userId: String,
        val mediaContentType: MediaContentType,
        val isMicEnabled: Boolean,
        val isSpeaking: Boolean
    ) : MediaMessage

    data class VideoStateChange(
        val userId: String,
        val mediaContentType: MediaContentType,
        val isVideoEnabled: Boolean,
    ) : MediaMessage

}