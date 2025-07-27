package com.youhajun.data.user

sealed class UserEndpoint(val path: String) {
    data object MyInfo : UserEndpoint("api/users/me")
}