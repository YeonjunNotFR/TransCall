package com.youhajun.transcall.fcm

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.youhajun.core.design.R
import com.youhajun.core.notification.NotificationService
import com.youhajun.core.notification.notification.DefaultNotification
import com.youhajun.core.route.DeepLinkEntry
import com.youhajun.feature.history.api.ConversationSyncLauncher
import com.youhajun.feature.main.MainActivity
import com.youhajun.transcall.fcm.dto.ConversationSyncPayload
import com.youhajun.transcall.fcm.dto.CreateRoomPayload
import com.youhajun.transcall.fcm.dto.FcmMessage
import com.youhajun.transcall.fcm.dto.FcmPayload
import com.youhajun.transcall.fcm.dto.HistoryDetailPayload
import com.youhajun.transcall.fcm.dto.HistoryPayload
import com.youhajun.transcall.fcm.dto.NotificationPayload
import com.youhajun.transcall.fcm.dto.ProfilePayload
import com.youhajun.transcall.fcm.dto.RoomListPayload
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TransCallFCMService : FirebaseMessagingService() {

    @Inject
    lateinit var conversationSyncLauncher: ConversationSyncLauncher

    @Inject
    lateinit var notificationService: NotificationService


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val dataJson = JSONObject(message.data).toString()
        val dto = fcmMessageJson.decodeFromString<FcmMessage>(dataJson)

        handleFcmMessage(dto, message.notification)
    }

    private fun handleFcmMessage(dto: FcmMessage, notification: RemoteMessage.Notification?) {
        when (val payload = dto.payload) {
            is ConversationSyncPayload -> {
                conversationSyncLauncher.launch(
                    roomId = payload.roomId,
                    joinedAt = payload.joinedAt,
                    leftAt = payload.leftAt
                )
            }

            is NotificationPayload,
            is HistoryPayload,
            is HistoryDetailPayload,
            is ProfilePayload,
            is CreateRoomPayload,
            is RoomListPayload -> {
                if (notification != null) {
                    val uri = payload.toDeepLinkEntryUri()
                    val pending = contentPendingIntent(uri)
                    val appNotification = DefaultNotification(
                        title = notification.title ?: getString(R.string.app_name),
                        content = notification.body ?: "",
                        open = pending,
                    )
                    notificationService.publish(appNotification)
                }
            }
        }
    }

    private fun FcmPayload.toDeepLinkEntryUri(): Uri = when(this) {
        is HistoryDetailPayload -> DeepLinkEntry.HistoryDetail(historyId).toUri()
        HistoryPayload -> DeepLinkEntry.HistoryList.toUri()
        ProfilePayload -> DeepLinkEntry.Profile.toUri()
        CreateRoomPayload -> DeepLinkEntry.CreateRoom.toUri()
        RoomListPayload -> DeepLinkEntry.RoomList.toUri()
        else -> DeepLinkEntry.Home.toUri()
    }

    private fun contentPendingIntent(uri: Uri): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = uri
        }
        val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(this, uri.hashCode(), intent, flag)
    }

    companion object {
        private val fcmMessageJson: Json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            classDiscriminator = "type"
            serializersModule = SerializersModule {
                polymorphic(FcmPayload::class) {
                    subclass(NotificationPayload::class)
                    subclass(ConversationSyncPayload::class)
                    subclass(HistoryPayload::class)
                    subclass(HistoryDetailPayload::class)
                    subclass(ProfilePayload::class)
                    subclass(CreateRoomPayload::class)
                    subclass(RoomListPayload::class)
                }

            }
        }
    }
}