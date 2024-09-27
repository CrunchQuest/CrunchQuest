package com.crunchquest.android.domain.entities

data class Booking(
    val bookingId: String,
    val customerId: String,
    val serviceId: String,
    val providerId: String,
    val bookingDate: String,
    val bookingTime: String,
    val bookingStatus: String,
    val notes: String?,
    val paymentStatus: String,
    val totalPrice: Double,
    val address: String,
    val captureResult: String?,
    val captureResultTimestamp: String?,
    val createdAt: String,
    val updatedAt: String,
    val providerConfirmed: Boolean,
    val customerConfirmed: Boolean,
    val reviewed: Boolean
)
