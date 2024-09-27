package com.crunchquest.android.domain.entities

data class Service(
    val serviceId: String,
    val providerId: String,
    val serviceName: String,
    val serviceImage: String?,
    val serviceDescription: String,
    val price: Int,
    val category: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
