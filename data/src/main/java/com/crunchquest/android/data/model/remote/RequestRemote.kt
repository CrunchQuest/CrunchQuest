package com.crunchquest.android.data.model.remote

data class RequestRemote(
    val requestId: String = "",
    val requesterId: String = "",
    val assistantUserId: String? = null,
    val title: String = "",
    val description: String = "",
    val categoryId: String = "",
    val status: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val taskSchedule: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val rewards: Double?,
    val notes: String? = null,
    val paymentStatus: String = "",
    val paymentMethod: String?,
    val requesterConfirmed: Boolean = false,
    val assistantConfirmed: Boolean = false,
    val reviewed: Boolean = false,
    val captureResult: String? = null,
    val captureResultTimestamp: String? = null
)
