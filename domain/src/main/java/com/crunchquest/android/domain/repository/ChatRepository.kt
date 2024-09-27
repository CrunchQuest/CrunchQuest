package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Chat

interface ChatRepository {
    suspend fun getChat(chatId: String): Result<Chat>
    suspend fun createChat(chat: Chat): Result<Chat>
    suspend fun getChatsByRequest(requestId: String): Result<List<Chat>>
    suspend fun updateChat(chat: Chat): Result<Unit>
    suspend fun deleteChat(chatId: String): Result<Unit>
}
