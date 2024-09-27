package com.crunchquest.android.data.model.remote

data class ProviderRemote(
    val providerId: String = "",
    val userId: String = "",
    val credentialsUrl: String? = null,
    val licenses: String? = null,
    val serviceArea: String = "",
    val paymentDetails: String? = null,
    val termsAccepted: Boolean = false,
    val termsAcceptedDate: String = "",
    val rating: Double = 0.0,
    val status: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)