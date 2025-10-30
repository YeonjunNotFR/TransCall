package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.local.MicChunk
import kotlin.math.log10
import kotlin.math.sqrt

internal object SoundUtil {

    private const val MIN_DB = -70.0
    private const val MAX_DB = -25.0

    fun calculateAudioLevel(chunk: MicChunk): Float {
        val audioData = chunk.audioData
        if (audioData.isEmpty()) return 0.0f

        val normalizedRms = calculateRms16Bit(audioData)

        val logLevel = if (normalizedRms > 0.00001) {
            20 * log10(normalizedRms)
        } else {
            MIN_DB
        }

        val clampedDb = logLevel.coerceIn(MIN_DB, MAX_DB)
        val level01 = ((clampedDb - MIN_DB) / (MAX_DB - MIN_DB)) * 0.8f + 0.1f

        return level01.toFloat()
    }

    private fun calculateRms16Bit(audioData: ByteArray): Double {
        val bytesPerSample = 2
        val sampleCount = audioData.size / bytesPerSample

        if (sampleCount == 0) return 0.0

        var sumSquares = 0.0

        for (i in 0 until sampleCount) {
            val byteIndex = i * bytesPerSample

            if (byteIndex + 1 < audioData.size) {
                val low = audioData[byteIndex].toInt() and 0xFF
                val high = audioData[byteIndex + 1].toInt() shl 8
                val sample = (high or low).toShort().toDouble()
                sumSquares += sample * sample
            }
        }

        val rms = sqrt(sumSquares / sampleCount)
        return rms / 32767.0
    }
}