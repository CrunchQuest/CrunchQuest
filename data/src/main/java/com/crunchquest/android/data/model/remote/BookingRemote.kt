package com.crunchquest.android.data.model.remote

data class BookingRemote(
    val bookingId: String = "",
    val customerId: String = "",
    val serviceId: String = "",
    val providerId: String = "",
    val bookingDate: String = "",
    val bookingTime: String = "",
    val bookingStatus: String = "",
    val notes: String? = null,
    val paymentStatus: String = "",
    val totalPrice: Double = 0.0,
    val address: String = "",
    val captureResult: String? = null,
    val captureResultTimestamp: String? = null,
    val createdAt: String = "",
    val updatedAt: String = "",
    val providerConfirmed: Boolean = false,
    val customerConfirmed: Boolean = false,
    val reviewed: Boolean = false
)