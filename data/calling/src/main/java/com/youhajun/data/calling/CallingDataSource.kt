package com.youhajun.data.calling

import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.core.network.di.WebSocketHttpClient
import com.youhajun.data.common.dto.calling.ClientMessageDto
import com.youhajun.data.common.dto.calling.ServerMessageDto
import com.youhajun.data.common.dto.calling.TurnCredentialDto
import com.youhajun.data.common.dto.calling.payload.CameraEnableChangedDto
import com.youhajun.data.common.dto.calling.payload.ChangedRoomDto
import com.youhajun.data.common.dto.calling.payload.CompleteIceCandidateDto
import com.youhajun.data.common.dto.calling.payload.ConnectedRoomDto
import com.youhajun.data.common.dto.calling.payload.JoinRoomPublisherDto
import com.youhajun.data.common.dto.calling.payload.JoinRoomSubscriberDto
import com.youhajun.data.common.dto.calling.payload.JoinedRoomPublisherDto
import com.youhajun.data.common.dto.calling.payload.MediaStateChangedDto
import com.youhajun.data.common.dto.calling.payload.MediaStateInitDto
import com.youhajun.data.common.dto.calling.payload.MicEnableChangedDto
import com.youhajun.data.common.dto.calling.payload.OnIceCandidateDto
import com.youhajun.data.common.dto.calling.payload.OnNewPublisherDto
import com.youhajun.data.common.dto.calling.payload.OnUnpublishedDto
import com.youhajun.data.common.dto.calling.payload.PublisherAnswerDto
import com.youhajun.data.common.dto.calling.payload.PublisherOfferDto
import com.youhajun.data.common.dto.calling.payload.RequestPayloadDto
import com.youhajun.data.common.dto.calling.payload.ResponsePayloadDto
import com.youhajun.data.common.dto.calling.payload.SignalingIceCandidateDto
import com.youhajun.data.common.dto.calling.payload.SttStartDto
import com.youhajun.data.common.dto.calling.payload.SubscriberAnswerDto
import com.youhajun.data.common.dto.calling.payload.SubscriberOfferDto
import com.youhajun.data.common.dto.calling.payload.SubscriberUpdateDto
import com.youhajun.data.common.dto.calling.payload.TranslationMessageDto
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
import kotlinx.serialization.modules.subclass
import timber.log.Timber
import javax.inject.Inject

internal interface CallingDataSource {
    fun connect(roomId: String): Flow<ServerMessageDto>
    suspend fun sendClientMessage(message: ClientMessageDto)
    suspend fun sendBinaryMessage(data: ByteArray)
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
                    Timber.tag("WebSocket").d("Received: $text")
                    val dto = signalingJson.decodeFromString<ServerMessageDto>(text)
                    emit(dto)
                }
            }
        }
    }

    override suspend fun getTurnCredential(): TurnCredentialDto {
        return restClient.get(CallingEndpoint.TurnCredential.path).body()
    }

    override suspend fun sendClientMessage(message: ClientMessageDto) {
        val json = signalingJson.encodeToString(message)
        Timber.tag("WebSocket").d("Sending: $json")
        session?.send(Frame.Text(json))
    }

    override suspend fun sendBinaryMessage(data: ByteArray) {
        session?.send(Frame.Binary(true, data))
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
                    subclass(JoinedRoomPublisherDto::class)
                    subclass(PublisherAnswerDto::class)
                    subclass(SubscriberOfferDto::class)
                    subclass(OnIceCandidateDto::class)
                    subclass(OnNewPublisherDto::class)
                    subclass(OnUnpublishedDto::class)
                    subclass(ConnectedRoomDto::class)
                    subclass(ChangedRoomDto::class)
                    subclass(SttStartDto::class)
                    subclass(TranslationMessageDto::class)
                    subclass(MediaStateInitDto::class)
                    subclass(MediaStateChangedDto::class)
                }

                polymorphic(RequestPayloadDto::class) {
                    classDiscriminator = "action"
                    subclass(JoinRoomPublisherDto::class)
                    subclass(PublisherOfferDto::class)
                    subclass(JoinRoomSubscriberDto::class)
                    subclass(SubscriberAnswerDto::class)
                    subclass(SubscriberUpdateDto::class)
                    subclass(SignalingIceCandidateDto::class)
                    subclass(CompleteIceCandidateDto::class)
                    subclass(MicEnableChangedDto::class)
                    subclass(CameraEnableChangedDto::class)
                }
            }
        }
    }
}