package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.VideoRoomHandleInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoRoomHandleInfoDto(
    @SerialName("defaultPublisherHandleId")
    val defaultPublisherHandleId: Long,
    @SerialName("screenSharePublisherHandleId")
    val screenSharePublisherHandleId: Long,
    @SerialName("subscriberHandleId")
    val subscriberHandleId: Long
) {
    fun toModel(): VideoRoomHandleInfo = VideoRoomHandleInfo(
        defaultPublisherHandleId = defaultPublisherHandleId,
        screenSharePublisherHandleId = screenSharePublisherHandleId,
        subscriberHandleId = subscriberHandleId
    )
}