package com.crunchquest.android.domain.entities

data class Chat(
    val chatId: String,
    val requestId: String,
    val members: List<String>
)
