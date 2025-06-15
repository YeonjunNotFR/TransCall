package com.youhajun.data.common.pagination

import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.pagination.Node
import com.youhajun.core.model.pagination.PageInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CursorPageDto<T>(
    @SerialName("edges")
    val edges: List<NodeDto<T>> = emptyList(),
    @SerialName("pageInfo")
    val pageInfo: PageInfoDto? = null,
    @SerialName("totalCount")
    val totalCount: Int? = null
) {
    fun <M> toModel(mapper: (T) -> M): CursorPage<M> = CursorPage(
        edges = edges.map { Node(it.node?.let(mapper), it.cursor) },
        pageInfo = pageInfo?.let { PageInfo(it.hasNextPage, it.nextCursor) },
        totalCount = totalCount
    )
}

@Serializable
data class NodeDto<T>(
    @SerialName("node")
    val node: T? = null,
    @SerialName("cursor")
    val cursor: String? = null
)

@Serializable
data class PageInfoDto(
    @SerialName("hasNextPage")
    val hasNextPage: Boolean? = null,
    @SerialName("nextCursor")
    val nextCursor: String? = null
)