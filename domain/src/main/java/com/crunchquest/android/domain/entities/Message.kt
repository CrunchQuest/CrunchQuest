package com.crunchquest.android.domain.entities

data class Message(
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: String
)
