package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.MessageRemote
import com.crunchquest.android.data.source.local.MessageLocal
import com.crunchquest.android.domain.entities.Message
import javax.inject.Inject

class MessageMapper @Inject constructor() {

    // Convert remote data model to domain entity
    fun fromRemote(messageRemote: MessageRemote): com.crunchquest.android.domain.entities.Message {
        return com.crunchquest.android.domain.entities.Message(
            messageId = messageRemote.messageId,
            chatId = messageRemote.chatId,
            senderId = messageRemote.senderId,
            content = messageRemote.content,
            timestamp = messageRemote.timestamp,
            status = messageRemote.status
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(message: com.crunchquest.android.domain.entities.Message): MessageRemote {
        return MessageRemote(
            messageId = message.messageId,
            chatId = message.chatId,
            senderId = message.senderId,
            content = message.content,
            timestamp = message.timestamp,
            status = message.status
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(messageLocal: MessageLocal): com.crunchquest.android.domain.entities.Message {
        return com.crunchquest.android.domain.entities.Message(
            messageId = messageLocal.messageId,
            chatId = messageLocal.chatId,
            senderId = messageLocal.senderId,
            content = messageLocal.content,
            timestamp = messageLocal.timestamp,
            status = messageLocal.status
        )
    }

    // Convert domain entity to local data model
    fun toLocal(message: com.crunchquest.android.domain.entities.Message): MessageLocal {
        return MessageLocal(
            messageId = message.messageId,
            chatId = message.chatId,
            senderId = message.senderId,
            content = message.content,
            timestamp = message.timestamp,
            status = message.status
        )
    }
}
