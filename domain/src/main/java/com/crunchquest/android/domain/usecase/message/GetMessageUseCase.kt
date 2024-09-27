package com.crunchquest.android.domain.usecase.message

import com.crunchquest.android.domain.entities.Message
import com.crunchquest.android.domain.repository.MessageRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(chatId: String): Result<List<Message>> {
        return messageRepository.getMessage(chatId)
    }
}
