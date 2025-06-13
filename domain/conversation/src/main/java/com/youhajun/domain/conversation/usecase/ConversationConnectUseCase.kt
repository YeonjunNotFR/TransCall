package com.youhajun.domain.conversation.usecase

import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.conversation.ConversationMessage
import com.youhajun.core.model.conversation.ConversationMessageType
import com.youhajun.domain.conversation.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationConnectUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    operator fun invoke(roomCode: String): Flow<ConversationMessage> {
        return repository.connect(roomCode).onEach {
            when(val type = it.type) {
                is Conversation -> {
                    repository.upsertConversation(type)
                }
                else -> Unit
            }
        }
    }
}