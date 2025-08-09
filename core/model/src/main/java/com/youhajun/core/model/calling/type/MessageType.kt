package com.youhajun.core.model.calling.type;

enum class MessageType(val type: String) {
    UNKNOWN("unknown"),
    SIGNALING("signaling"),
    TRANSLATION("translation"),
    MEDIA_STATE("media_state"),
    ROOM("room");

    companion object {
        fun fromType(type: String): MessageType {
            return entries.firstOrNull { it.type == type } ?: UNKNOWN
        }
    }
}