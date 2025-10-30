package com.youhajun.webrtc.model.local

internal data class MicChunk(
    val audioData: ByteArray,
    val sampleRate: Int,
    val numberOfChannels: Int,
    val numberOfFrames: Int
) {
    override fun equals(other: Any?) =
        other is MicChunk &&
                sampleRate == other.sampleRate &&
                numberOfChannels == other.numberOfChannels &&
                numberOfFrames == other.numberOfFrames &&
                audioData.contentEquals(other.audioData)

    override fun hashCode(): Int {
        var result = sampleRate
        result = 31 * result + numberOfChannels
        result = 31 * result + numberOfFrames
        result = 31 * result + audioData.contentHashCode()
        return result
    }
}