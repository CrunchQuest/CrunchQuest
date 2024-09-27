package com.crunchquest.android.domain.entities

data class Payment(
    val paymentId: String,
    val requestId: String,
    val payerId: String,
    val payeeId: String,
    val amount: Double,
    val paymentMethod: String,
    val transactionReference: String?,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val releaseDate: String?
)
