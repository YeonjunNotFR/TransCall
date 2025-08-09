package com.youhajun.core.model.calling

import com.youhajun.core.model.room.CurrentParticipant
import kotlinx.collections.immutable.ImmutableList

data class CallHistory(
    val historyId: String,
    val title: String,
    val memo: String,
    val summary: String,
    val isLiked: Boolean,
    val startedAtToEpochTime: Long,
    val endedAtToEpochTime: Long,
    val durationSeconds: Int,
    val currentParticipants: ImmutableList<CurrentParticipant>,
)