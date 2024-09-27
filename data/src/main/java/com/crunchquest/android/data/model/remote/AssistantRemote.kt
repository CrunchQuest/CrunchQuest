package com.crunchquest.android.data.model.remote

data class AssistantRemote(
    val assistantId: String = "",
    val requestId: String = "",
    val assistantUserId: String = "",
    val assistanceStatus: String = "",
    val proposedRewards: Double? = null,
    val notes: String? = null,
    val taskAvailability: String = "",
    val assistConfirmed: Boolean = false,
    val paymentRequested: Boolean = false,
    val startedExecuting: Boolean = false,
    val createdAt: String = "",
    val updatedAt: String = ""
)