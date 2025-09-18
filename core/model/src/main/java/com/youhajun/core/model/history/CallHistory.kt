package com.youhajun.core.model.history

import com.youhajun.core.model.room.Participant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CallHistory(
    val historyId: String = "",
    val roomId: String = "",
    val title: String = "",
    val memo: String = "",
    val summary: String = "",
    val isLiked: Boolean = false,
    val joinedAtToEpochTime: Long = 0,
    val leftAtToEpochTime: Long? = 0,
    val durationSeconds: Int? = 0,
    val participants: ImmutableList<Participant> = persistentListOf(),
)