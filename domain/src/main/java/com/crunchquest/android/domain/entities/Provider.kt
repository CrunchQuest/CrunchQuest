package com.crunchquest.android.domain.entities

data class Provider(
    val providerId: String,
    val userId: String,
    val credentialsUrl: String?,
    val licenses: String?,
    val serviceArea: String,
    val paymentDetails: String?,
    val termsAccepted: Boolean,
    val termsAcceptedDate: String,
    val rating: Double,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
