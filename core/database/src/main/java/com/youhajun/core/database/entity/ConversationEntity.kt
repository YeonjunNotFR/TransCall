package com.youhajun.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EntityTable.CONVERSATION)
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val roomId: String,
    val senderId: String,
    val state: String,
    val originText: String,
    val originLanguageCode: String,
    val transText: String?,
    val transLanguageCode: String?,
    val updatedAtToEpochTime: Long,
    val createdAtToEpochTime: Long
)