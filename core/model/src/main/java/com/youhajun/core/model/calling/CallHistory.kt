package com.youhajun.core.model.calling

import com.youhajun.core.model.room.Participant
import kotlinx.collections.immutable.ImmutableList

data class CallHistory(
    val historyId: String,
    val title: String,
    val memo: String,
    val summary: String,
    val isLiked: Boolean,
    val startedAt: Long,
    val endedAt: Long,
    val durationSeconds: Int,
    val participants: ImmutableList<Participant>,
)