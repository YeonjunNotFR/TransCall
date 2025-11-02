package com.youhajun.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EntityTable.PARTICIPANT)
data class ParticipantEntity(
    @PrimaryKey
    val participantId: String,
    val userId: String,
    val roomId: String,
    val displayName: String,
    val languageCode: String,
    val countryCode: String,
    val imageUrl: String? = null,
    val leftAtToEpochTime: Long?
)