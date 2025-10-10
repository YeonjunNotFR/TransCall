package com.youhajun.feature.history.api

interface ConversationSyncLauncher {
    fun launch(roomId: String, joinedAt: Long, leftAt: Long)
}