package com.crunchquest.android.data.model.remote

data class ChatRemote(
    val chatId: String = "",
    val requestId: String = "",
    val members: List<String> = emptyList(),
)