package com.crunchquest.android.domain.entities

data class AssistantList(
    val assistantListId: String,
    val requestId: String,
    val arrayAssistantUserId: List<String>,
    val assistanceStatus: String,
    val isSelectedByRequester: Boolean,
    val createdAt: String,
    val updatedAt: String
)
