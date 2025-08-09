package com.youhajun.core.model.room

enum class RoomStatus(val type: String) {
    WAITING("waiting"),
    IN_PROGRESS("in_progress"),
    ENDED("ended");

    companion object {
        fun fromType(type: String): RoomStatus {
            return entries.firstOrNull { it.type == type } ?: WAITING
        }
    }
}