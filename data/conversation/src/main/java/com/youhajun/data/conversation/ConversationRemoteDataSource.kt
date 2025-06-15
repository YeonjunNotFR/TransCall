package com.youhajun.data.conversation

import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.core.network.di.WebSocketHttpClient
import com.youhajun.data.common.pagination.CursorPageDto
import com.youhajun.data.common.parametersFrom
import com.youhajun.data.conversation.dto.ConversationDataDto
import com.youhajun.data.conversation.dto.ConversationDto
import com.youhajun.data.conversation.dto.ConversationMessageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import javax.inject.Inject

internal interface ConversationRemoteDataSource {
    fun connect(roomCode: String): Flow<ConversationMessageDto>
    suspend fun send(message: ConversationMessageDto)
    suspend fun close()

    suspend fun getConversationList(request: CursorPageRequest, roomCode: String): CursorPageDto<ConversationDto>
}

internal class ConversationRemoteDataSourceImpl @Inject constructor(
    @WebSocketHttpClient private val wsClient: HttpClient,
    @RestHttpClient private val restClient: HttpClient,
) : ConversationRemoteDataSource {

    private var session: WebSocketSession? = null

    override fun connect(roomCode: String): Flow<ConversationMessageDto> = flow {
        wsClient.webSocket(urlString = ConversationEndpoint.Conversation(roomCode).path) {
            session = this

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val dto = conversationJson.decodeFromString<ConversationMessageDto>(frame.readText())
                    emit(dto)
                }
            }
        }
    }

    override suspend fun send(message: ConversationMessageDto) {
        val json = conversationJson.encodeToString(message)
        session?.send(Frame.Text(json))
    }

    override suspend fun close() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "대화 종료"))
        session = null
    }

    override suspend fun getConversationList(request: CursorPageRequest, roomCode: String): CursorPageDto<ConversationDto> {
        return restClient.get(ConversationEndpoint.List(roomCode).path) {
            parametersFrom(request)
        }.body()
    }

    companion object {
        private val conversationJson: Json = Json {
            serializersModule = SerializersModule {
                polymorphic(ConversationDataDto::class) {
                    classDiscriminator = "type"
                    subclass(ConversationDataDto.SttMessageDto::class, ConversationDataDto.SttMessageDto.serializer())
                    subclass(ConversationDto::class, ConversationDto.serializer())
                }
            }
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}