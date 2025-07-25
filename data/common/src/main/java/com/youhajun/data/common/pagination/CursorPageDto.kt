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
    val pageInfo: PageInfoDto = PageInfoDto(),
    @SerialName("totalCount")
    val totalCount: Int = -1
) {
    fun <M> toModel(mapper: (T) -> M): CursorPage<M> = CursorPage(
        edges = edges.mapNotNull { nodeDto ->
            nodeDto.node?.let { Node(node = mapper(it), cursor = nodeDto.cursor) }
        },
        pageInfo = PageInfo(pageInfo.hasNextPage, pageInfo.nextCursor),
        totalCount = totalCount
    )
}

@Serializable
data class NodeDto<T>(
    @SerialName("node")
    val node: T? = null,
    @SerialName("cursor")
    val cursor: String = ""
)

@Serializable
data class PageInfoDto(
    @SerialName("hasNextPage")
    val hasNextPage: Boolean = false,
    @SerialName("nextCursor")
    val nextCursor: String = ""
)