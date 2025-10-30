package com.youhajun.webrtc.video

import org.webrtc.CapturerObserver

internal interface CameraController {
    val isFrontCamera: Boolean
    fun initCapture(capturerObserver: CapturerObserver)
    fun startCapture(): Result<Unit>
    fun stopCapture(): Result<Unit>
    fun switchCamera(callback: (Boolean) -> Unit)
    fun dispose()
}