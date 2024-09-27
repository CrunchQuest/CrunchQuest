package com.crunchquest.android.data.model.remote

data class MessageRemote(
    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = 0,
    val status: String = ""
)