package com.youhajun.feature.call.impl.model

import com.youhajun.core.model.room.Participant
import com.youhajun.webrtc.model.CallMediaUser

data class CallUserUiModel(
    val participant: Participant?,
    val mediaUser: CallMediaUser,
    val mediaKey: String = mediaUser.userId,
    val userId: String = mediaUser.key,
)