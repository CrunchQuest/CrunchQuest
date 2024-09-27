package com.crunchquest.android.data.model.remote

data class ReviewRemote(
    val reviewId: String = "",
    val requestId: String = "",
    val reviewerId: String = "",
    val reviewedId: String = "",
    val rating: Int = 0,
    val reviewText: String? = null,
    val createdAt: String = "",
    val updatedAt: String = ""
)