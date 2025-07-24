package com.youhajun.core.model.pagination

data class CursorPage<T>(
    val edges: List<Node<T>>,
    val pageInfo: PageInfo,
    val totalCount: Int
)

data class Node<T>(
    val node: T,
    val cursor: String
)

data class PageInfo(
    val hasNextPage: Boolean,
    val nextCursor: String
)