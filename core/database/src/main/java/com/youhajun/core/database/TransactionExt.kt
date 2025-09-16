package com.youhajun.core.database

import androidx.room.withTransaction
import javax.inject.Inject

class TransactionProvider @Inject internal constructor(
    private val db: TransCallRoomDataBase
) {
    suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }
}