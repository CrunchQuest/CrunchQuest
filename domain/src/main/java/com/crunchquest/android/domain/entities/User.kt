package com.crunchquest.android.domain.entities

data class User(
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val bio: String?,
    val avgRating: Double?,
    val dateOfBirth: String?,
    val gender: String?,
    val phoneNumber: String,
    val profilePicture: String?,
    val idCard: String?,
    val preference: String?,
    val createdAt: String,
    val updatedAt: String,
    val lastLogin: String?,
    val status: String,
    val role: String,
    val verificationStatus: String?,
    val workExperience: String?
)
