package com.youhajun.core.model;

enum class SortDirection(val type: String) {
    ASC("ASC"),
    DESC("DESC");

    fun toggle(): SortDirection = when (this) {
        ASC -> DESC
        DESC -> ASC
    }

    companion object {
        fun fromType(type: String): SortDirection =
            entries.firstOrNull { it.type == type.uppercase() } ?: DESC
    }
}