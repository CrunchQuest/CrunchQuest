package com.crunchquest.android.data.remote.model

data class RegisterResponse(
    val user: UserResponse
) {
    data class UserResponse(
        val userId: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val verificationToken: String,
        val emailVerified: Boolean,
        val updatedAt: String,
        val createdAt: String
    )
}