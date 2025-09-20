package com.youhajun.webrtc.audio

class SoundLevelSmoother {

    companion object {
        private const val ATTACK_RATE = 0.8f    // 올라갈 때 속도 (0.1-1.0)
        private const val DECAY_RATE = 0.09f    // 내려갈 때 속도 (0.01-0.1)
        private const val PEAK_HOLD_TIME = 8    // 피크 유지 시간 (프레임 수)
        private const val MINIMUM_THRESHOLD = 0.15f // 이 값 이하는 0.1로 처리
    }

    private var smoothedLevel = 0.1f
    private var peakHoldLevel = 0.1f
    private var peakHoldCounter = 0

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
}
