package com.crunchquest.android.data.remote.model

data class UserResponse(
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val emailVerified: Boolean
)