package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.ChatRemote
import com.crunchquest.android.data.source.local.ChatLocal
import com.crunchquest.android.domain.entities.Chat
import com.google.android.play.integrity.internal.c
import javax.inject.Inject

class ChatMapper @Inject constructor() {

    // Convert remote data model to domain entity
    fun fromRemote(chatRemote: ChatRemote): Chat {
        return Chat(
            chatId = chatRemote.chatId,
            requestId = chatRemote.requestId,
            members = chatRemote.members
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(chat: Chat): ChatRemote {
        return ChatRemote(
            chatId = chat.chatId,
            requestId = chat.requestId,
            members = chat.members
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(chatLocal: ChatLocal): Chat {
        return Chat(
            chatId = chatLocal.chatId,
            requestId = chatLocal.requestId,
            members = chatLocal.members
        )
    }

    // Convert domain entity to local data model
    fun toLocal(chat: Chat): ChatLocal {
        return ChatLocal(
            chatId = chat.chatId,
            requestId = chat.requestId,
            members = chat.members
        )
    }
}
