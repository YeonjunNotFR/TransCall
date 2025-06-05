package com.youhajun.core.model.room

enum class RoomType(val type: String) {
    CODE_JOIN("code"),
    DIRECT_CALL("direct");

    companion object {
        fun fromType(type: String?): RoomType {
            return entries.firstOrNull { it.type == type } ?: CODE_JOIN
        }
    }
}