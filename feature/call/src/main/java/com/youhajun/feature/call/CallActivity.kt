package com.youhajun.feature.call

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.youhajun.core.design.TransCallTheme
import com.youhajun.core.route.rememberNavigationEventHandler
import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.feature.call.api.CallNavRoute
import com.youhajun.feature.call.navigation.callNavGraph
import com.youhajun.feature.call.service.CallForegroundService
import com.youhajun.feature.call.service.CallServiceContract
import com.youhajun.feature.call.service.LocalCallServiceContract
import com.youhajun.transcall.core.ui.locals.LocalEglBaseContext
import dagger.hilt.android.AndroidEntryPoint
import org.webrtc.EglBase
import javax.inject.Inject

@AndroidEntryPoint
class CallActivity : ComponentActivity() {

    companion object {
        const val INTENT_KEY_ROOM_ID = "room_id"
    }

    @Inject
    lateinit var callIntentFactory: CallIntentFactory

    @Inject
    lateinit var eglBaseContext: EglBase.Context

    private var callServiceContract: CallServiceContract? by mutableStateOf(null)
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as? CallForegroundService.LocalBinder
            callServiceContract = localBinder?.getContract()
            localBinder?.onFinish { finish() }
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            callServiceContract?.leaveCall()
            callServiceContract = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roomId = intent.getStringExtra(INTENT_KEY_ROOM_ID) ?: throw IllegalArgumentException("Room code is required")
        startCallService(roomId)

        setContent {
            TransCallTheme {
                val navigator = rememberNavController()
                val navigationEventHandler = rememberNavigationEventHandler(navigator)

                CompositionLocalProvider(
                    LocalEglBaseContext provides eglBaseContext,
                    LocalCallServiceContract provides callServiceContract
                ) {
                    NavHost(
                        modifier = Modifier.fillMaxSize().safeContentPadding(),
                        navController = navigator,
                        startDestination = CallNavRoute.Calling(roomId)
                    ) {
                        callNavGraph(navigationEventHandler::handleNavigationEvent)
                    }
                }
            }
        }
    }

    private fun startCallService(roomId: String) {
        val foregroundServiceIntent = callIntentFactory.getCallServiceIntent(this, roomId)
        ContextCompat.startForegroundService(this, foregroundServiceIntent)
        bindService(foregroundServiceIntent, serviceConnection, BIND_AUTO_CREATE)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}