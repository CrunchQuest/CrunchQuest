package com.crunchquest.android.domain.entities

data class Review(
    val reviewId: String,
    val requestId: String,
    val reviewerId: String,
    val reviewedId: String,
    val rating: Int,
    val reviewText: String?,
    val createdAt: String,
    val updatedAt: String
)
