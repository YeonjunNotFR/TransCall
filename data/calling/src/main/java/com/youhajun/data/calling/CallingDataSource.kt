package com.youhajun.data.calling

import com.youhajun.core.network.di.WebSocketHttpClient
import com.youhajun.data.calling.dto.CallingDataDto
import com.youhajun.data.calling.dto.CallingMessageDto
import com.youhajun.data.calling.dto.StageDataDto
import com.youhajun.data.calling.dto.ConnectDataDto
import com.youhajun.data.calling.dto.JoinedDataDto
import com.youhajun.data.calling.dto.LeftResponseDto
import com.youhajun.data.calling.dto.SignalingDataDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import javax.inject.Inject

internal interface CallingDataSource {
    fun connect(roomId: String): Flow<CallingMessageDto>
    suspend fun send(message: CallingMessageDto)
    suspend fun close()
}

internal class CallingDataSourceImpl @Inject constructor(
    @WebSocketHttpClient private val client: HttpClient,
) : CallingDataSource {

    private var session: WebSocketSession? = null

    override fun connect(roomId: String): Flow<CallingMessageDto> = flow {
        client.webSocket(urlString = CallingEndpoint.Calling(roomId).path) {
            session = this

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val dto = signalingJson.decodeFromString<CallingMessageDto>(frame.readText())
                    emit(dto)
                }
            }
        }
    }

    override suspend fun send(message: CallingMessageDto) {
        val json = signalingJson.encodeToString(message)
        session?.send(Frame.Text(json))
    }

    override suspend fun close() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "통화 종료"))
        session = null
    }

    companion object {
        private val signalingJson: Json = Json {
            serializersModule = SerializersModule {
                polymorphic(CallingDataDto::class) {
                    classDiscriminator = "type"
                    subclass(SignalingDataDto.OfferDto::class, SignalingDataDto.OfferDto.serializer())
                    subclass(SignalingDataDto.AnswerDto::class, SignalingDataDto.AnswerDto.serializer())
                    subclass(SignalingDataDto.CandidateDto::class, SignalingDataDto.CandidateDto.serializer())
                    subclass(JoinedDataDto::class, JoinedDataDto.serializer())
                    subclass(LeftResponseDto::class, LeftResponseDto.serializer())
                    subclass(ConnectDataDto::class, ConnectDataDto.serializer())
                    subclass(StageDataDto.ErrorDto::class, StageDataDto.ErrorDto.serializer())
                    subclass(StageDataDto.WaitingDto::class, StageDataDto.WaitingDto.serializer())
                    subclass(StageDataDto.SignalingDto::class, StageDataDto.SignalingDto.serializer())
                    subclass(StageDataDto.CallingDto::class, StageDataDto.CallingDto.serializer())
                    subclass(StageDataDto.EndedDto::class, StageDataDto.EndedDto.serializer())
                }
            }
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}