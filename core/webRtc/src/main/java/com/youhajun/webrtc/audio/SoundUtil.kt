package com.youhajun.webrtc.audio

import android.media.AudioFormat
import com.youhajun.webrtc.model.MicChunk
import kotlin.math.log10
import kotlin.math.round
import kotlin.math.sqrt

object SoundUtil {

    private const val MIN_DB = -70.0
    private const val MAX_DB = -25.0

    private const val ATTACK_RATE = 0.8f    // 올라갈 때 속도 (0.1-1.0)
    private const val DECAY_RATE = 0.09f    // 내려갈 때 속도 (0.01-0.1)
    private const val PEAK_HOLD_TIME = 8    // 피크 유지 시간 (프레임 수)
    private const val MINIMUM_THRESHOLD = 0.15f // 이 값 이하는 0.1로 처리

    private var smoothedLevel = 0.1f
    private var peakHoldLevel = 0.1f
    private var peakHoldCounter = 0

    fun calculateAudioLevel(chunk: MicChunk): Float {
        val audioData = chunk.audioData
        if (audioData.isEmpty()) return 0.0f

        val normalizedRms = when (chunk.audioFormat) {
            AudioFormat.ENCODING_PCM_FLOAT -> calculateRmsFloat(audioData)
            AudioFormat.ENCODING_PCM_16BIT -> calculateRms16Bit(audioData)
            AudioFormat.ENCODING_PCM_8BIT -> calculateRms8Bit(audioData)
            else -> return 0.0f
        }

        val logLevel = if (normalizedRms > 0.00001) {
            20 * log10(normalizedRms)
        } else {
            MIN_DB
        }

        // -80dB ~ 0dB 범위를 0.1f ~ 0.9f로 매핑
        val clampedDb = logLevel.coerceIn(MIN_DB, MAX_DB)
        val level01 = ((clampedDb - MIN_DB) / (MAX_DB - MIN_DB)) * 0.8f + 0.1f

        return level01.toFloat()
    }

    fun getSmoothedLevel(rawLevel: Float): Float {
        val processedLevel = if (rawLevel < MINIMUM_THRESHOLD) 0.1f else rawLevel

        if (processedLevel > peakHoldLevel) {
            peakHoldLevel = processedLevel
            peakHoldCounter = PEAK_HOLD_TIME
        } else if (peakHoldCounter > 0) {
            peakHoldCounter--
            peakHoldLevel = maxOf(peakHoldLevel - 0.005f, processedLevel)
        } else {
            peakHoldLevel = processedLevel
        }

        // 3. 스무딩 적용
        smoothedLevel = when {
            peakHoldLevel > smoothedLevel -> {
                // 올라갈 때는 빠르게
                smoothedLevel + (peakHoldLevel - smoothedLevel) * ATTACK_RATE
            }
            else -> {
                // 내려갈 때는 천천히
                smoothedLevel + (peakHoldLevel - smoothedLevel) * DECAY_RATE
            }
        }

        return smoothedLevel.coerceIn(0.1f, 0.9f)
    }

    private fun calculateRmsFloat(audioData: ByteArray): Double {
        val bytesPerSample = 4 // 32비트 float
        val sampleCount = audioData.size / bytesPerSample

        if (sampleCount == 0) return 0.0

        var sumSquares = 0.0

        for (i in 0 until sampleCount) {
            val byteIndex = i * bytesPerSample

            if (byteIndex + 3 < audioData.size) {
                // Little-endian 32비트 float 읽기
                val intBits = (audioData[byteIndex].toInt() and 0xFF) or
                        ((audioData[byteIndex + 1].toInt() and 0xFF) shl 8) or
                        ((audioData[byteIndex + 2].toInt() and 0xFF) shl 16) or
                        ((audioData[byteIndex + 3].toInt() and 0xFF) shl 24)

                val sample = Float.fromBits(intBits).toDouble()
                sumSquares += sample * sample
            }
        }

        val rms = sqrt(sumSquares / sampleCount)
        return rms // Float PCM은 이미 -1.0 ~ 1.0 범위로 정규화됨
    }

    private fun calculateRms16Bit(audioData: ByteArray): Double {
        val bytesPerSample = 2
        val sampleCount = audioData.size / bytesPerSample

        if (sampleCount == 0) return 0.0

        var sumSquares = 0.0

        for (i in 0 until sampleCount) {
            val byteIndex = i * bytesPerSample

            if (byteIndex + 1 < audioData.size) {
                // Little-endian 16비트 샘플 읽기
                val low = audioData[byteIndex].toInt() and 0xFF
                val high = audioData[byteIndex + 1].toInt() shl 8
                val sample = (high or low).toShort().toDouble()
                sumSquares += sample * sample
            }
        }

        val rms = sqrt(sumSquares / sampleCount)
        return rms / 32767.0 // 16비트의 최대값으로 정규화
    }

    private fun calculateRms8Bit(audioData: ByteArray): Double {
        val sampleCount = audioData.size

        if (sampleCount == 0) return 0.0

        var sumSquares = 0.0

        for (i in audioData.indices) {
            // 8비트 PCM은 unsigned (0-255), 중앙값 128을 빼서 signed로 변환
            val sample = (audioData[i].toInt() and 0xFF) - 128
            sumSquares += sample * sample
        }

        val rms = sqrt(sumSquares / sampleCount)
        return rms / 127.0 // 8비트의 최대값으로 정규화
    }

    // 소수점 2자리 반올림(표시용)
    private fun Float.round2(): Float = (round(this * 100f) / 100f)
}