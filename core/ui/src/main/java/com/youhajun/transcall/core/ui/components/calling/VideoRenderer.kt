/*
 * Copyright (c) 2014-2023 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modified by yeonjun on 2025/06/04
 * Changes:
 * - add setMirror
 * - add videoScalingType
 */

package com.youhajun.transcall.core.ui.components.calling;

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.youhajun.transcall.core.ui.locals.LocalEglBaseContext
import io.getstream.webrtc.android.compose.VideoScalingType
import org.webrtc.RendererCommon
import org.webrtc.RendererCommon.RendererEvents
import org.webrtc.VideoTrack

@Composable
internal fun VideoRenderer(
    videoTrack: VideoTrack,
    modifier: Modifier = Modifier,
    isFrontCamera: Boolean = false,
    translucentOverlay: Boolean = false,
    videoScalingType: VideoScalingType = VideoScalingType.SCALE_ASPECT_BALANCED,
    rendererEvents: RendererEvents = object : RendererEvents {
        override fun onFirstFrameRendered() = Unit
        override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) = Unit
    }
) {
    val eglBaseContext = LocalEglBaseContext.current
    val trackState: MutableState<VideoTrack?> = remember { mutableStateOf(null) }
    var view: VideoSurfaceViewRenderer? by remember { mutableStateOf(null) }

    DisposableEffect(videoTrack) {
        onDispose {
            cleanTrack(view, trackState)
        }
    }

    AndroidView(
        factory = { context ->
            VideoSurfaceViewRenderer(context).apply {
                init(eglBaseContext, rendererEvents, translucentOverlay)
                setScalingType(videoScalingType.toCommonScalingType())
                setMirror(isFrontCamera)
                setupVideo(trackState, videoTrack, this)
                view = this
            }
        },
        update = { v ->
            v.setScalingType(videoScalingType.toCommonScalingType())
            v.setMirror(isFrontCamera)
            setupVideo(trackState, videoTrack, v)
        },
        modifier = modifier,
    )
}

private fun cleanTrack(
    view: VideoSurfaceViewRenderer?,
    trackState: MutableState<VideoTrack?>,
) {
    view?.let { trackState.value?.removeSink(it) }
    trackState.value = null
}

private fun setupVideo(
    trackState: MutableState<VideoTrack?>,
    track: VideoTrack,
    renderer: VideoSurfaceViewRenderer,
) {
    if (trackState.value == track) {
        return
    }

    cleanTrack(renderer, trackState)

    trackState.value = track
    track.addSink(renderer)
}

fun VideoScalingType.toCommonScalingType(): RendererCommon.ScalingType {
    return when (this) {
        VideoScalingType.SCALE_ASPECT_FIT -> RendererCommon.ScalingType.SCALE_ASPECT_FIT
        VideoScalingType.SCALE_ASPECT_FILL -> RendererCommon.ScalingType.SCALE_ASPECT_FILL
        VideoScalingType.SCALE_ASPECT_BALANCED -> RendererCommon.ScalingType.SCALE_ASPECT_BALANCED
    }
}