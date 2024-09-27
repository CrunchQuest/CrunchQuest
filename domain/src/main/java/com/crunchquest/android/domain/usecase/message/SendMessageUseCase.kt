package com.crunchquest.android.domain.usecase.message

import com.crunchquest.android.domain.entities.Message
import com.crunchquest.android.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(message: Message): Result<Message> {
        return messageRepository.sendMessage(message)
    }
}
