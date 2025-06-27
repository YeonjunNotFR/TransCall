package com.youhajun.feature.history.impl

import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.DateRange
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

data class HistoryState(
    val callHistoryDateMap: ImmutableMap<String, ImmutableList<CallHistory>> = persistentMapOf(),
    val selectedDateRange: DateRange = DateRange.Week,
)