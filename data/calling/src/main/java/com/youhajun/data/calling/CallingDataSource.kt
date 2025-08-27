package com.youhajun.data.calling

import android.util.Log
import com.youhajun.core.network.BuildConfig
import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.core.network.di.WebSocketHttpClient
import com.youhajun.data.calling.dto.ClientMessageDto
import com.youhajun.data.calling.dto.ServerMessageDto
import com.youhajun.data.calling.dto.TurnCredentialDto
import com.youhajun.data.calling.dto.payload.ChangedRoomDto
import com.youhajun.data.calling.dto.payload.CompleteIceCandidateDto
import com.youhajun.data.calling.dto.payload.ConnectedRoomDto
import com.youhajun.data.calling.dto.payload.JoinRoomPublisherDto
import com.youhajun.data.calling.dto.payload.JoinRoomSubscriberDto
import com.youhajun.data.calling.dto.payload.JoinedRoomPublisherDto
import com.youhajun.data.calling.dto.payload.OnIceCandidateDto
import com.youhajun.data.calling.dto.payload.OnNewPublisherDto
import com.youhajun.data.calling.dto.payload.PublisherAnswerDto
import com.youhajun.data.calling.dto.payload.PublisherOfferDto
import com.youhajun.data.calling.dto.payload.RequestPayloadDto
import com.youhajun.data.calling.dto.payload.ResponsePayloadDto
import com.youhajun.data.calling.dto.payload.SignalingIceCandidateDto
import com.youhajun.data.calling.dto.payload.SttMessageDto
import com.youhajun.data.calling.dto.payload.SttStartDto
import com.youhajun.data.calling.dto.payload.SubscriberAnswerDto
import com.youhajun.data.calling.dto.payload.SubscriberOfferDto
import com.youhajun.data.calling.dto.payload.SubscriberUpdateDto
import com.youhajun.data.calling.dto.payload.TranslationMessageDto
import com.youhajun.data.common.parametersFrom
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path
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
    fun connect(roomId: String): Flow<ServerMessageDto>
    suspend fun send(message: ClientMessageDto)
    suspend fun close()
    suspend fun getTurnCredential(): TurnCredentialDto
}

internal class CallingDataSourceImpl @Inject constructor(
    @WebSocketHttpClient private val client: HttpClient,
    @RestHttpClient private val restClient: HttpClient,
) : CallingDataSource {

    private var session: WebSocketSession? = null

    override fun connect(roomId: String): Flow<ServerMessageDto> = flow {
        client.webSocket(
            request = {
                url {
                    protocol = URLProtocol.WSS
                    path(CallingEndpoint.Calling.path)
                }
                parameter("roomId", roomId)
            }
        ) {
            session = this

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    if (BuildConfig.DEBUG) Log.d("WebSocket", "Received: $text")
                    val dto = signalingJson.decodeFromString<ServerMessageDto>(text)
                    emit(dto)
                }
            }
        }
    }

    override suspend fun getTurnCredential(): TurnCredentialDto {
        return restClient.get(CallingEndpoint.TurnCredential.path).body()
    }

    override suspend fun send(message: ClientMessageDto) {
        val json = signalingJson.encodeToString(message)
        if (BuildConfig.DEBUG) Log.d("WebSocket","Sending: $json")
        session?.send(Frame.Text(json))
    }

    override suspend fun close() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "통화 종료"))
        session = null
    }

    companion object {
        private val signalingJson: Json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            serializersModule = SerializersModule {
                polymorphic(ResponsePayloadDto::class) {
                    classDiscriminator = "action"
                    subclass(JoinedRoomPublisherDto::class, JoinedRoomPublisherDto.serializer())
                    subclass(PublisherAnswerDto::class, PublisherAnswerDto.serializer())
                    subclass(SubscriberOfferDto::class, SubscriberOfferDto.serializer())
                    subclass(OnIceCandidateDto::class, OnIceCandidateDto.serializer())
                    subclass(OnNewPublisherDto::class, OnNewPublisherDto.serializer())
                    subclass(ConnectedRoomDto::class, ConnectedRoomDto.serializer())
                    subclass(ChangedRoomDto::class, ChangedRoomDto.serializer())
                    subclass(SttStartDto::class, SttStartDto.serializer())
                    subclass(TranslationMessageDto::class, TranslationMessageDto.serializer())
                }

                polymorphic(RequestPayloadDto::class) {
                    classDiscriminator = "action"
                    subclass(JoinRoomPublisherDto::class, JoinRoomPublisherDto.serializer())
                    subclass(PublisherOfferDto::class, PublisherOfferDto.serializer())
                    subclass(JoinRoomSubscriberDto::class, JoinRoomSubscriberDto.serializer())
                    subclass(SubscriberAnswerDto::class, SubscriberAnswerDto.serializer())
                    subclass(SubscriberUpdateDto::class, SubscriberUpdateDto.serializer())
                    subclass(SignalingIceCandidateDto::class, SignalingIceCandidateDto.serializer())
                    subclass(SttMessageDto::class, SttMessageDto.serializer())
                    subclass(CompleteIceCandidateDto::class, CompleteIceCandidateDto.serializer())
                }
            }
        }
    }
}