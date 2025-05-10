package com.youhajun.core.model.pagination

data class OffsetPageRequest(
    val offset: Int,
    val limit: Int
) {
    fun isFirstCall(): Boolean = offset == 0
}