package com.youhajun.core.model.auth

data class Nonce(
    val loginRequestId: String,
    val nonce: String
)