package com.youhajun.core.model.room

enum class RoomJoinType(val type: String) {
    CODE_JOIN("code"),
    DIRECT_CALL("direct");

    companion object {
        fun fromType(type: String?): RoomJoinType {
            return entries.firstOrNull { it.type == type } ?: CODE_JOIN
        }
    }
}