package com.youhajun.feature.call.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.youhajun.core.design.R
import com.youhajun.core.notification.NotificationService
import com.youhajun.core.notification.notification.AppNotification
import com.youhajun.core.notification.notification.CallActions
import com.youhajun.core.notification.notification.CallNotification
import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.feature.call.api.service.CallServiceContract
import com.youhajun.feature.call.api.service.CallServiceBinder
import com.youhajun.webrtc.model.stream.LocalMediaUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallForegroundService : Service(), CallServiceManager.ServiceCallback {

    @Inject
    lateinit var serviceManager: CallServiceManager

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var callIntentFactory: CallIntentFactory

    private var currentRoomId: String? = null

    private var onFinish: (() -> Unit)? = null

    override fun onBind(intent: Intent?): IBinder = CallServiceBinderImpl()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(currentRoomId == null) {
            val roomId = intent?.getStringExtra(INTENT_KEY_ROOM_ID) ?: return START_REDELIVER_INTENT
            initStart(roomId)
            serviceManager.initManager(this)
            serviceManager.startCall(roomId)
        }
        handleAction(intent)
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceManager.disposeAll()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        serviceManager.disposeAll()
        stopSelf()
    }

    override fun onError(error: Throwable) {
        stopSelf()
        onFinish?.invoke()
    }

    override fun onLeave() {
        stopSelf()
        onFinish?.invoke()
    }

    override fun onMediaStateChanged(isMicEnable: Boolean, isCameraEnable: Boolean) {
        val roomId = currentRoomId ?: return
        val notification = createNotification(roomId)
        notificationService.publish(notification)
    }

    private fun initStart(roomId: String) {
        currentRoomId = roomId
        val appNotification = createNotification(roomId)
        val callNotificationId = appNotification.notificationId
        val notification = appNotification.build(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val flag = ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK or ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
            startForeground(callNotificationId, notification, flag)
        } else {
            startForeground(callNotificationId, notification)
        }
    }

    private fun handleAction(intent: Intent?) {
        when (intent?.action) {
            INTENT_ACTION_END -> {
                serviceManager.leaveCall()
            }
            INTENT_ACTION_MIC_TOGGLE -> {
                val micEnable = intent.getBooleanExtra(INTENT_EXTRA_MIC_TOGGLE, false)
                serviceManager.setMicEnabled(micEnable)
            }
            INTENT_ACTION_CAMERA_TOGGLE -> {
                val cameraEnable = intent.getBooleanExtra(INTENT_EXTRA_CAMERA_TOGGLE, false)
                serviceManager.setCameraEnabled(cameraEnable)
            }
            else -> Unit
        }
    }

    private fun createNotification(roomId: String): AppNotification {
        val localMediaUser = serviceManager.mediaUsersFlow.value.firstOrNull { it is LocalMediaUser }
        val isMicEnabled = localMediaUser?.audioStream?.isMicEnabled ?: true
        val isCameraEnabled = localMediaUser?.videoStream?.isVideoEnable ?: true

        val actions = CallActions(
            open = createOpenPendingIntent(roomId),
            end = createEndPendingIntent(),
            micToggle = createMicTogglePendingIntent(!isMicEnabled),
            cameraToggle = createCameraTogglePendingIntent(!isCameraEnabled)
        )
        return CallNotification(
            title = getString(R.string.notification_call_title),
            content = getString(R.string.notification_call_content),
            actions = actions,
            isMicEnabled = isMicEnabled,
            isCameraEnabled = isCameraEnabled
        )
    }

    private fun createOpenPendingIntent(roomId: String): PendingIntent {
        val intent = callIntentFactory.getCallActivityIntent(this, roomId).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingFlag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(this, 0, intent, pendingFlag)
    }

    private fun createEndPendingIntent(): PendingIntent {
        val intent = callIntentFactory.getCallServiceIntent(this).apply {
            action = INTENT_ACTION_END
        }
        val pendingFlag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getService(this, 1, intent, pendingFlag)
    }

    private fun createMicTogglePendingIntent(setMicEnable: Boolean): PendingIntent {
        val intent = callIntentFactory.getCallServiceIntent(this).apply {
            action = INTENT_ACTION_MIC_TOGGLE
            putExtra(INTENT_EXTRA_MIC_TOGGLE, setMicEnable)
        }
        val pendingFlag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getService(this, 2, intent, pendingFlag)
    }

    private fun createCameraTogglePendingIntent(setCameraEnable: Boolean): PendingIntent {
        val intent = callIntentFactory.getCallServiceIntent(this).apply {
            action = INTENT_ACTION_CAMERA_TOGGLE
            putExtra(INTENT_EXTRA_CAMERA_TOGGLE, setCameraEnable)
        }
        val pendingFlag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getService(this, 3, intent, pendingFlag)
    }

    companion object {
        const val INTENT_KEY_ROOM_ID = "ROOM_ID"
        private const val INTENT_ACTION_END = "END"
        private const val INTENT_ACTION_MIC_TOGGLE = "MIC_TOGGLE"
        private const val INTENT_EXTRA_MIC_TOGGLE = "EXTRA_MIC_TOGGLE"
        private const val INTENT_ACTION_CAMERA_TOGGLE = "CAMERA_TOGGLE"
        private const val INTENT_EXTRA_CAMERA_TOGGLE = "EXTRA_CAMERA_TOGGLE"
    }

    internal inner class CallServiceBinderImpl : CallServiceBinder() {
        override fun getContract(): CallServiceContract = serviceManager
        override fun onFinish(finish: () -> Unit) { onFinish = finish }
        override fun ongoingCallRoomId(): String? = currentRoomId
    }
}