package com.youhajun.data.common.dto.calling

import com.youhajun.core.model.calling.TurnCredential
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TurnCredentialDto(
    @SerialName("url")
    val url: String,
    @SerialName("username")
    val username: String,
    @SerialName("credential")
    val credential: String,
) {
    fun toModel() = TurnCredential(
        url = url,
        username = username,
        credential = credential
    )
}