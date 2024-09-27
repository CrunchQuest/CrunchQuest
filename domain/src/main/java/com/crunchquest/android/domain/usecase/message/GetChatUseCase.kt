package com.crunchquest.android.domain.usecase.message

import com.crunchquest.android.domain.entities.Chat
import com.crunchquest.android.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Result<Chat> {
        return chatRepository.getChat(chatId)
    }
}
