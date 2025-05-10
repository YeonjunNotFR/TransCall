package com.youhajun.domain.history.usecase

import com.youhajun.domain.history.HistoryRepository
import javax.inject.Inject

class DeleteHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(callId: String): Result<Unit> {
        return repository.deleteHistory(callId)
    }
}
