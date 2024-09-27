package com.crunchquest.android.domain.entities

data class Assistant(
    val assistantId: String,
    val requestId: String,
    val assistantUserId: String,
    val assistanceStatus: String,
    val proposedRewards: Double?,
    val notes: String?,
    val taskAvailability: String,
    val assistConfirmed: Boolean,
    val paymentRequested: Boolean,
    val startedExecuting: Boolean,
    val createdAt: String,
    val updatedAt: String
)

