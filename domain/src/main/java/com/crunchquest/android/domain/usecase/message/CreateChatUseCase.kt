package com.crunchquest.android.domain.usecase.message

import com.crunchquest.android.domain.entities.Chat
import com.crunchquest.android.domain.repository.ChatRepository
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chat: Chat): Result<Chat> {
        return chatRepository.createChat(chat)
    }
}
