package com.youhajun.data.common.pagination

import com.youhajun.core.model.pagination.OffsetPage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OffsetPageDto<T>(
    @SerialName("data")
    val data: List<T> = emptyList(),
    @SerialName("offset")
    val offset: Int = 0,
    @SerialName("limit")
    val limit: Int = 20,
    @SerialName("total")
    val total: Int? = null,
    @SerialName("hasMore")
    val hasMore: Boolean? = null
) {
    fun <M>toModel(mapper: (T) -> M): OffsetPage<M> = OffsetPage(
        data = data.map { mapper(it) },
        offset = offset,
        limit = limit,
        total = total,
        hasMore = hasMore
    )
}