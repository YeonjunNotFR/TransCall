package com.youhajun.data.common.dto.auth

import com.youhajun.core.model.auth.Nonce
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NonceDto(
    @SerialName("loginRequestId")
    val loginRequestId: String,
    @SerialName("nonce")
    val nonce: String
) {
    fun toModel(): Nonce = Nonce(
        loginRequestId = loginRequestId,
        nonce = nonce
    )
}