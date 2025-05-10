package com.youhajun.domain.history

import com.youhajun.core.model.CallHistory

interface HistoryRepository {
    suspend fun getHistoryList(): Result<List<CallHistory>>
    suspend fun getHistoryDetail(callId: String): Result<CallHistory>
    suspend fun deleteHistory(callId: String): Result<Unit>
}