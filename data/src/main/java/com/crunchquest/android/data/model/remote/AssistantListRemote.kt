package com.crunchquest.android.data.model.remote

data class AssistantListRemote(
    val assistantListId: String,
    val requestId: String,
    val arrayAssistantUserId: List<String>,
    val assistanceStatus: String,
    val isSelectedByRequester: Boolean,
    val createdAt: String,
    val updatedAt: String
)