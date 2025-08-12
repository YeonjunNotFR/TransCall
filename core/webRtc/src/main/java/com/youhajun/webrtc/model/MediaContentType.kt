package com.youhajun.webrtc.model

internal enum class MediaContentType(val type: String) {
    DEFAULT("default"), SCREEN_SHARE("screenShare");

    companion object {
        fun fromType(type: String): MediaContentType {
            return entries.firstOrNull { it.type == type } ?: DEFAULT
        }
    }
}