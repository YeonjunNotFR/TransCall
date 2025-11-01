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

        // RMS를 데시벨(dB) 단위로 변환한다.
        // log10 스케일을 사용해 인간의 청각 특성을 반영.
        val logLevel = if (normalizedRms > 0.00001) {
            20 * log10(normalizedRms)
        } else {
            MIN_DB
        }

        // 최소/최대 dB 범위 내로 클램프(clamp)
        val clampedDb = logLevel.coerceIn(MIN_DB, MAX_DB)
        val level01 = ((clampedDb - MIN_DB) / (MAX_DB - MIN_DB)) * 0.8f + 0.1f

        return level01.toFloat()
    }

    /**
     * 오디오 데이터의 RMS(root mean square) 값을 구함
     * RMS = sqrt(Σ(sample²) / N)
     * → 각 샘플의 제곱 평균을 통해 오디오 에너지 크기를 계산.
     * → 32767.0으로 나누어 [-1.0, 1.0] 사이로 정규화.
     */

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