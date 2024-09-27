package com.crunchquest.android.data.remote.dto

data class UpdateUserRequest(
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val bio: String?
)