package com.youhajun.core.model.calling.payload

sealed interface MediaStateResponse : ResponsePayload

data class MediaStateInit(
    val mediaStateList: List<MediaState>
) : MediaStateResponse

data class MediaStateChanged(
    val mediaState: MediaState
) : MediaStateResponse

data class MediaState(
    val userId: String,
    val micEnabled: Boolean,
    val cameraEnabled: Boolean,
)