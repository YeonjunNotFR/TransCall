package com.youhajun.webrtc.model.local

internal sealed interface LocalVideoEvent {
    data class CameraEnabledChanged(val enabled: Boolean) : LocalVideoEvent
}