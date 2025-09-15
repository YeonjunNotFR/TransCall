package com.youhajun.core.database.entity

import androidx.room.Entity

@Entity(
    tableName = EntityTable.CONVERSATION_META,
    primaryKeys = ["roomId", "joinedAt"],
)
data class ConversationMetaEntity(
    val roomId: String,
    val joinedAt: Long,
    val lastSyncedAt: Long? = null,
)