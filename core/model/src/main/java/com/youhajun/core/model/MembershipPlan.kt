package com.youhajun.core.model

enum class MembershipPlan(val id: String) {
    Free("free"),
    Pro("pro"),
    Premium("premium");

    companion object {
        fun fromId(id: String): MembershipPlan = entries.firstOrNull { it.id == id } ?: Free
    }
}