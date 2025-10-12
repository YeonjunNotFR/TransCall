package com.youhajun.feature.history.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.youhajun.feature.history.api.ConversationSyncLauncher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationSyncLauncherImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ConversationSyncLauncher {

    override fun launch(roomId: String, joinedAt: Long, leftAt: Long) {
        val input = workDataOf(
            ConversationSyncWorker.KEY_ROOM_ID to roomId,
            ConversationSyncWorker.KEY_JOINED_AT to joinedAt,
            ConversationSyncWorker.KEY_LEFT_AT to leftAt
        )

        val request = OneTimeWorkRequestBuilder<ConversationSyncWorker>()
            .setInputData(input)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                getConversationWorkName(roomId, joinedAt),
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                request
            )
    }

    companion object {
        private fun getConversationWorkName(roomId: String, joinedAt: Long): String {
            return "conversation_sync-$roomId-$joinedAt"
        }
    }
}