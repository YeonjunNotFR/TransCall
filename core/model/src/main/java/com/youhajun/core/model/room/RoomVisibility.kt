package com.youhajun.core.model.room

enum class RoomVisibility(val type: String) {
    PUBLIC("public"),
    PRIVATE("private");

    companion object {
        fun fromType(type: String?): RoomVisibility {
            return entries.firstOrNull { it.type == type } ?: PUBLIC
        }
    }
}