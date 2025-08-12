package com.youhajun.webrtc.model

sealed interface MediaMessage {
    data class AudioStateChange(
        val userId: String,
        val mediaContentType: String,
        val isMicEnabled: Boolean,
        val isSpeaking: Boolean
    ) : MediaMessage

    data class VideoStateChange(
        val userId: String,
        val mediaContentType: String,
        val isVideoEnabled: Boolean,
    ) : MediaMessage

}