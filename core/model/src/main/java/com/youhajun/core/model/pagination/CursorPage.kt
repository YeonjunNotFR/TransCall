package com.youhajun.core.model.pagination

data class CursorPage<T>(
    val edges: List<Node<T>> = emptyList(),
    val pageInfo: PageInfo? = null,
    val totalCount: Int? = null
)

data class Node<T>(
    val node: T? = null,
    val cursor: String? = null
)

data class PageInfo(
    val hasNextPage: Boolean? = null,
    val nextCursor: String? = null
)