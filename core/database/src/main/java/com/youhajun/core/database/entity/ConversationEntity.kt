package com.youhajun.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EntityTable.CONVERSATION)
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val roomId: String,
    @Embedded(prefix = "sender_")
    val senderInfo: SenderInfoEntity,
    val originText: String,
    val transText: String?,
    val transLanguageCode: String,
    val timestamp: Long
)

data class SenderInfoEntity(
    val id: String,
    val displayName: String,
    val languageCode: String,
    val profileUrl: String? = null,
)