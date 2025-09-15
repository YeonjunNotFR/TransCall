package com.youhajun.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = EntityTable.CONVERSATION_CURSOR,
)
data class ConversationCursorEntity(
    @PrimaryKey
    val conversationId: String,
    val historyCursor: String
)