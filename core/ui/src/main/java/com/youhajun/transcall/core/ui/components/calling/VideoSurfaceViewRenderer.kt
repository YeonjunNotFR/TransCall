package com.youhajun.transcall.core.ui.components.calling

import android.content.Context
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import org.webrtc.EglBase
import org.webrtc.EglRenderer
import org.webrtc.GlRectDrawer
import org.webrtc.RendererCommon.RendererEvents
import org.webrtc.RendererCommon.ScalingType
import org.webrtc.RendererCommon.VideoLayoutMeasure
import org.webrtc.ThreadUtils
import org.webrtc.VideoFrame
import org.webrtc.VideoSink
import java.util.concurrent.CountDownLatch

internal class VideoSurfaceViewRenderer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SurfaceView(context, attrs), VideoSink, SurfaceHolder.Callback {

    /** Cached resource name */
    private val resourceName: String = getResourceName()

    /** Measure helper */
    private val videoLayoutMeasure = VideoLayoutMeasure()

    /** EGL-backed renderer */
    private val eglRenderer: EglRenderer = EglRenderer(resourceName)

    /** Render event listener */
    private var rendererEvents: RendererEvents? = null

    /** UI thread handler */
    private val uiThreadHandler = Handler(Looper.getMainLooper())

    /** First-frame flag */
    private var isFirstFrameRendered = false

    /** Rotated frame info */
    private var rotatedFrameWidth = 0
    private var rotatedFrameHeight = 0
    private var frameRotation = 0

    init {
        holder.addCallback(this)
    }

    /** Mirror control */
    fun setMirror(mirror: Boolean) {
        eglRenderer.setMirror(mirror)
    }

    /** Scaling type (single) */
    fun setScalingType(scalingType: ScalingType?) {
        ThreadUtils.checkIsOnMainThread()
        videoLayoutMeasure.setScalingType(scalingType)
        requestLayout()
    }

    /** Scaling type (match/mismatch) */
    fun setScalingType(
        scalingTypeMatchOrientation: ScalingType?,
        scalingTypeMismatchOrientation: ScalingType?,
    ) {
        ThreadUtils.checkIsOnMainThread()
        videoLayoutMeasure.setScalingType(
            scalingTypeMatchOrientation,
            scalingTypeMismatchOrientation
        )
        requestLayout()
    }

    /** VideoSink */
    override fun onFrame(videoFrame: VideoFrame) {
        eglRenderer.onFrame(videoFrame)
        updateFrameData(videoFrame)
    }

    /** Measure with current rotated frame size */
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        ThreadUtils.checkIsOnMainThread()
        val size = videoLayoutMeasure.measure(
            widthSpec, heightSpec, rotatedFrameWidth, rotatedFrameHeight
        )
        setMeasuredDimension(size.x, size.y)
    }

    /** Keep frame metadata & dispatch events */
    private fun updateFrameData(videoFrame: VideoFrame) {
        if (!isFirstFrameRendered) {
            rendererEvents?.onFirstFrameRendered()
            isFirstFrameRendered = true
        }
        if (videoFrame.rotatedWidth != rotatedFrameWidth ||
            videoFrame.rotatedHeight != rotatedFrameHeight ||
            videoFrame.rotation != frameRotation
        ) {
            rotatedFrameWidth = videoFrame.rotatedWidth
            rotatedFrameHeight = videoFrame.rotatedHeight
            frameRotation = videoFrame.rotation

            post { requestLayout() }

            uiThreadHandler.post {
                rendererEvents?.onFrameResolutionChanged(
                    rotatedFrameWidth, rotatedFrameHeight, frameRotation
                )
            }
        }
    }

    /** Keep aspect ratio in renderer */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        eglRenderer.setLayoutAspectRatio((right - left) / (bottom.toFloat() - top))
    }

    /**
     * Initialize EGL/GL resources.
     *
     * @param sharedContext EglBase.Context
     * @param rendererEvents listener
     * @param translucent 투명 배경이 필요하면 true (오버레이 등). 필요 없으면 기본값 false 권장.
     */
    fun init(
        sharedContext: EglBase.Context,
        rendererEvents: RendererEvents,
        translucentOverlay: Boolean = false,
    ) {
        ThreadUtils.checkIsOnMainThread()
        this.rendererEvents = rendererEvents

        if (translucentOverlay) {
            setZOrderMediaOverlay(true)
            holder.setFormat(PixelFormat.TRANSLUCENT)
        }

        val config = if (translucentOverlay) EglBase.CONFIG_RGBA else EglBase.CONFIG_PLAIN
        eglRenderer.init(sharedContext, config, GlRectDrawer())
    }

    /** FPS 0으로 감소 */
    fun pauseVideo() {
        eglRenderer.pauseVideo()
    }

    /** FPS 복원 */
    fun resumeVideo() {
        eglRenderer.disableFpsReduction()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        eglRenderer.createEglSurface(holder.surface)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        val latch = CountDownLatch(1)
        eglRenderer.releaseEglSurface { latch.countDown() }
        ThreadUtils.awaitUninterruptibly(latch)
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int,
    ) {}

    override fun onDetachedFromWindow() {
        eglRenderer.release()
        super.onDetachedFromWindow()
    }

    private fun getResourceName(): String =
        try { resources.getResourceEntryName(id) + ": " }
        catch (_: Resources.NotFoundException) { "" }
}
