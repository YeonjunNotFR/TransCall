package com.youhajun.feature.call.component

import android.app.PictureInPictureParams
import android.graphics.Rect
import android.os.Build
import android.util.Rational
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.toRect
import com.youhajun.transcall.core.common.getActivity

@Composable
fun AutoEnterPipPreS(shouldAutoEnter: Boolean) {
    if (Build.VERSION.SDK_INT in Build.VERSION_CODES.O until Build.VERSION_CODES.S) {
        val activity = LocalContext.current.getActivity()
        val current by rememberUpdatedState(shouldAutoEnter)

        DisposableEffect(activity) {
            val r = Runnable {
                if (current) {
                    activity?.enterPictureInPictureMode(
                        PictureInPictureParams.Builder().setAspectRatio(Rational(12, 24)).build()
                    )
                }
            }

            activity?.addOnUserLeaveHintListener(r)
            onDispose { activity?.removeOnUserLeaveHintListener(r) }
        }
    }
}

@Composable
fun Modifier.autoEnterPipModifier(shouldAutoEnter: Boolean, isCameraEnable: Boolean, isMicEnable: Boolean): Modifier {
    val context = LocalContext.current
    var sourceRect by remember { mutableStateOf<Rect?>(null) }

    LaunchedEffect(isMicEnable, isCameraEnable, sourceRect) {
        val builder = PictureInPictureParams.Builder().apply {
            sourceRect?.let { setSourceRectHint(it) }
            setAspectRatio(Rational(12, 24))
            setActions(listOfRemoteActions(context, isCameraEnable, isMicEnable))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setAutoEnterEnabled(shouldAutoEnter)
                setSeamlessResizeEnabled(false)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                setExpandedAspectRatio(null)
            }
        }.build()

        context.getActivity()?.setPictureInPictureParams(builder)
    }

    return this.then(
        Modifier.onGloballyPositioned { layoutCoordinates ->
            sourceRect = layoutCoordinates.boundsInWindow().toAndroidRectF().toRect()
        }
    )
}