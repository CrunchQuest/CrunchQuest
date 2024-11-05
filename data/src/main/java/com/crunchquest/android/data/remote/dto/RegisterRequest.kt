package com.crunchquest.android.data.remote.dto

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val idImageUri: String?,
    val selfieImageUri: String?
)