package com.youhajun.webrtc.model.local

internal enum class TrackType(val type: String) {
    VIDEO("video"),
    AUDIO("audio");

    companion object {
        fun fromString(type: String): TrackType {
            return entries.firstOrNull { it.type == type } ?: VIDEO
        }
    }
}