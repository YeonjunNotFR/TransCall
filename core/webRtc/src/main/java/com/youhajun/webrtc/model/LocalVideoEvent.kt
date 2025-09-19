package com.youhajun.webrtc.model

sealed interface LocalVideoEvent {
    data class CameraEnabledChanged(val enabled: Boolean) : LocalVideoEvent
}
