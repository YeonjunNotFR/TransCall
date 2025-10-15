package com.youhajun.feature.call.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import com.youhajun.feature.call.model.CallControlAction
import com.youhajun.feature.call.model.CallingPipAction

@Composable
fun PipBroadcastReceiver(
    isInPipMode: Boolean,
    onClickAction: (CallControlAction) -> Unit
) {
    if (isInPipMode) {
        val context = LocalContext.current
        DisposableEffect(key1 = context) {
            val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if ((intent == null) || (intent.action != CallingPipAction.BROADCAST)) return

                    val type = intent.getStringExtra(CallingPipAction.CONTROL_TYPE)
                    val callControlAction = when (CallingPipAction.fromType(type)) {
                        CallingPipAction.MIC_ENABLE -> CallControlAction.ToggleMicEnable(false)
                        CallingPipAction.MIC_DISABLE -> CallControlAction.ToggleMicEnable(true)
                        CallingPipAction.CAMERA_ENABLE -> CallControlAction.ToggleCameraEnable(false)
                        CallingPipAction.CAMERA_DISABLE -> CallControlAction.ToggleCameraEnable(true)
                        CallingPipAction.LEAVE_CALL -> CallControlAction.LeaveCall
                    }
                    onClickAction(callControlAction)
                }
            }
            ContextCompat.registerReceiver(
                context,
                broadcastReceiver,
                IntentFilter(CallingPipAction.BROADCAST),
                RECEIVER_NOT_EXPORTED,
            )
            onDispose {
                context.unregisterReceiver(broadcastReceiver)
            }
        }
    }
}