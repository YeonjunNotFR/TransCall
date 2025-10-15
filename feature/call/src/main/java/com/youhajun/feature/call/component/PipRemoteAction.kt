package com.youhajun.feature.call.component

import android.app.PendingIntent
import android.app.RemoteAction
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.youhajun.core.design.R
import com.youhajun.feature.call.model.CallingPipAction

fun listOfRemoteActions(context: Context, isCameraEnable: Boolean, isMicEnable: Boolean): List<RemoteAction> {
    val micIconRes = if(isMicEnable) R.drawable.ic_call_mic_on else R.drawable.ic_call_mic_off
    val micTextRes = if(isMicEnable) R.string.notification_mic_on else R.string.notification_mic_off
    val micPipAction = if(isMicEnable) CallingPipAction.MIC_DISABLE else CallingPipAction.MIC_ENABLE
    val cameraIconRes = if(isCameraEnable) R.drawable.ic_call_camera_on else R.drawable.ic_call_camera_off
    val cameraTextRes = if(isCameraEnable) R.string.notification_camera_on else R.string.notification_camera_off
    val cameraPipAction = if(isCameraEnable) CallingPipAction.CAMERA_DISABLE else CallingPipAction.CAMERA_ENABLE

    return listOf(
        buildRemoteAction(
            micIconRes,
            micTextRes,
            micPipAction.request,
            micPipAction.type,
            context,
        ),
        buildRemoteAction(
            R.drawable.ic_leave_call,
            R.string.notification_call_end,
            CallingPipAction.LEAVE_CALL.request,
            CallingPipAction.LEAVE_CALL.type,
            context,
        ),
        buildRemoteAction(
            cameraIconRes,
            cameraTextRes,
            cameraPipAction.request,
            cameraPipAction.type,
            context,
        ),
    )
}

private fun buildRemoteAction(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
    requestCode: Int,
    controlType: String,
    context: Context,
): RemoteAction {
    val icon = Icon
        .createWithResource(context, iconResId)
        .setTint(ContextCompat.getColor(context, R.color.color_white))

    return RemoteAction(
        icon,
        context.getString(titleResId),
        context.getString(titleResId),
        PendingIntent.getBroadcast(
            context, requestCode,
            Intent(CallingPipAction.BROADCAST)
                .setPackage(context.packageName)
                .putExtra(CallingPipAction.CONTROL_TYPE, controlType),
            PendingIntent.FLAG_IMMUTABLE,
        ),
    )
}