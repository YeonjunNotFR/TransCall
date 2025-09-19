package com.youhajun.webrtc.model

sealed interface MediaStateMessage

sealed interface MediaStateMessageRequest : MediaStateMessage
sealed interface MediaStateMessageResponse : MediaStateMessage

data class MicEnableChanged(
    val isMicEnabled: Boolean,
) : MediaStateMessageRequest

data class CameraEnabledChanged(
    val isCameraEnabled: Boolean
) : MediaStateMessageRequest

data class MediaStateChanged(
    val mediaState: MediaState
) : MediaStateMessageResponse

data class MediaStateInit(
    val mediaStateList: List<MediaState>
) : MediaStateMessageResponse

data class MediaState(
    val userId: String,
    val micEnabled: Boolean,
    val cameraEnabled: Boolean,
)