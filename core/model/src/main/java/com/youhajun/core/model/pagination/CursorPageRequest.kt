package com.youhajun.core.model.pagination

data class CursorPageRequest(
    val after: String? = null,
    val first: Int,
)