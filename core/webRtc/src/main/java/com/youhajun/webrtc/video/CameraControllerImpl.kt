package com.youhajun.webrtc.video

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import org.webrtc.Camera2Capturer
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerationAndroid
import org.webrtc.CameraVideoCapturer
import org.webrtc.CapturerObserver
import org.webrtc.EglBase
import org.webrtc.SurfaceTextureHelper
import javax.inject.Inject

internal class CameraControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eglBaseContext: EglBase.Context,
) : CameraController {

    private val cameraManager by lazy { context.getSystemService<CameraManager>() }
    private val cameraEnumerator by lazy { Camera2Enumerator(context) }
    private val frontCameraId: String by lazy { getFrontCameraOrElseId() }
    override val isFrontCamera: Boolean
        get() = isFrontCameraId(frontCameraId)

    private val surfaceTextureHelper by lazy {
        SurfaceTextureHelper.create("CameraTextureHelper", eglBaseContext)
    }

    private val capturer: Camera2Capturer by lazy {
        Camera2Capturer(context, frontCameraId, null)
    }

    private val resolution: CameraEnumerationAndroid.CaptureFormat
        get() {
            val supportedFormats = cameraEnumerator.getSupportedFormats(frontCameraId).orEmpty()
            return findMatchingResolution(supportedFormats)
        }

    override fun initCapture(capturerObserver: CapturerObserver) {
        capturer.initialize(surfaceTextureHelper, context, capturerObserver)
    }

    override fun startCapture(): Result<Unit> {
        return runCatching { capturer.startCapture(resolution.width, resolution.height, 30) }
    }

    override fun stopCapture(): Result<Unit> {
        return runCatching { capturer.stopCapture() }
    }

    override fun switchCamera(callback: (Boolean) -> Unit) {
        capturer.switchCamera(object : CameraVideoCapturer.CameraSwitchHandler {
            override fun onCameraSwitchDone(isFrontCamera: Boolean) = callback(isFrontCamera)
            override fun onCameraSwitchError(errorDescription: String) {}
        })
    }

    override fun dispose() {
        stopCapture()
        capturer.dispose()
    }

    private fun findFrontFacingCameraId(cameraManager: CameraManager): String? {
        val cameraIds = cameraManager.cameraIdList

        for (cameraId in cameraIds) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)

            if (lensFacing == CameraMetadata.LENS_FACING_FRONT) {
                return cameraId
            }
        }

        return null
    }


    private fun getFrontCameraOrElseId(): String {
        val cameraManager = cameraManager ?: throw RuntimeException("CameraManager was not initialized!")
        val frontFacingCameraId = findFrontFacingCameraId(cameraManager)
        return frontFacingCameraId ?: getFirstAvailableCameraId(cameraManager)
    }

    private fun getFirstAvailableCameraId(cameraManager: CameraManager): String {
        val cameraIds = cameraManager.cameraIdList
        if (cameraIds.isEmpty()) {
            throw RuntimeException("No available cameras.")
        }
        return cameraIds.first()
    }

    private fun findMatchingResolution(supportedFormats: List<CameraEnumerationAndroid.CaptureFormat>): CameraEnumerationAndroid.CaptureFormat {
        return supportedFormats.find {
            (it.width == 720 || it.width == 480 || it.width == 360)
        } ?: supportedFormats.firstOrNull() ?: error("There is no matched resolution!")
    }


    private fun isFrontCameraId(cameraId: String): Boolean {
        val characteristics = cameraManager?.getCameraCharacteristics(cameraId)
        val lensFacing = characteristics?.get(CameraCharacteristics.LENS_FACING)
        return lensFacing == CameraMetadata.LENS_FACING_FRONT
    }
}