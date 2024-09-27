package com.crunchquest.android.data.model.remote

data class PaymentRemote(
    val paymentId: String = "",
    val requestId: String = "",
    val payerId: String = "",
    val payeeId: String = "",
    val amount: Double = 0.0,
    val paymentMethod: String = "",
    val transactionReference: String? = null,
    val status: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val releaseDate: String? = null
)