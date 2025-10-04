package com.youhajun.domain.calling.usecase

import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.core.model.calling.payload.ChangedRoom
import com.youhajun.core.model.calling.payload.ConnectedRoom
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.domain.calling.CallingRepository
import com.youhajun.domain.conversation.usecase.UpsertConversationUseCase
import com.youhajun.domain.room.usecase.UpsertRoomParticipantsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallConnectUseCase @Inject constructor(
    private val repository: CallingRepository,
    private val upsertConversationUseCase: UpsertConversationUseCase,
    private val upsertRoomParticipantsUseCase: UpsertRoomParticipantsUseCase
) {
    operator fun invoke(roomId: String): Flow<ServerMessage> {
        return repository.connect(roomId).onEach {
            when(val payload = it.payload) {
                is ConnectedRoom -> upsertRoomParticipantsUseCase(roomId, payload.participants)

                is ChangedRoom -> upsertRoomParticipantsUseCase(roomId, payload.participants)

                is TranslationMessage -> upsertConversationUseCase(payload)
                else -> Unit
            }
        }
    }
}