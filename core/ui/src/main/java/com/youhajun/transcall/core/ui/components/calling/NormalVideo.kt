package com.youhajun.transcall.core.ui.components.calling

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

@Composable
fun NormalVideo(
    modifier: Modifier = Modifier,
    videoTrack: VideoTrack?,
    isFrontCamera: Boolean = true,
    enabled: Boolean = true,
    rendererEvent: RendererCommon.RendererEvents = object : RendererCommon.RendererEvents {
        override fun onFirstFrameRendered() = Unit
        override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) = Unit
    },
    placeholder: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        if(enabled && videoTrack != null) {
            VideoRenderer(
                modifier = Modifier.fillMaxSize(),
                videoTrack = videoTrack,
                isFrontCamera = isFrontCamera,
                rendererEvents = rendererEvent,
            )
        } else {
            placeholder()
        }
    }
}