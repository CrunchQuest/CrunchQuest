package com.crunchquest.android.data.model.remote

data class ServiceAvailabilityRemote(
    val availabilityId: String = "",
    val serviceId: String = "",
    val dayOfWeek: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val status: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)