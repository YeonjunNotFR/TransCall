package com.youhajun.webrtc.model.local

internal sealed interface LocalAudioEvent {
    data class MicEnableChanged(val enabled: Boolean) : LocalAudioEvent
}
