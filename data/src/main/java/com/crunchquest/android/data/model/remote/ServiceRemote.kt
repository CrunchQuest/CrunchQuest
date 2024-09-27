package com.crunchquest.android.data.model.remote

data class ServiceRemote(
    val serviceId: String = "",
    val providerId: String = "",
    val serviceName: String = "",
    val serviceDescription: String = "",
    val price: Int = 0,
    val category: String = "",
    val status: String = "",
    val serviceImage: String? = null,
    val createdAt: String = "",
    val updatedAt: String = ""
)

