package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Message

interface MessageRepository {
    suspend fun sendMessage(message: Message): Result<Message>
    suspend fun getMessage(messageId: String): Result<List<Message>>
    suspend fun getMessagesByChat(chatId: String): Result<List<Message>>
    suspend fun updateMessageStatus(message: Message): Result<Unit>
    suspend fun deleteMessage(messageId: String): Result<Unit>
}

