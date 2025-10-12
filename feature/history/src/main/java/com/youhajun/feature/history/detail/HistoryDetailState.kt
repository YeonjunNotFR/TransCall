package com.youhajun.feature.history.detail

import com.youhajun.core.model.history.CallHistory

data class HistoryDetailState(
    val history: CallHistory = CallHistory(),
    val myUserId: String = "",
)