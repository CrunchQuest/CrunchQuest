package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.MessageRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MessageRemoteDataSource(private val firestore: FirebaseFirestore) {

    suspend fun sendMessage(messageRemote: MessageRemote) {
        firestore.collection("messages").document(messageRemote.messageId).set(messageRemote).await()
    }

    suspend fun getMessage(messageId: String): MessageRemote? {
        val messageDocument = firestore.collection("messages").document(messageId).get().await()
        return if (messageDocument.exists()) messageDocument.toObject(MessageRemote::class.java) else null
    }

    suspend fun getMessagesByChat(chatId: String): List<MessageRemote>? {
        val result = firestore.collection("messages")
            .whereEqualTo("chatId", chatId)
            .get()
            .await()
        return result.toObjects(MessageRemote::class.java)
    }

    suspend fun updateMessageStatus(messageRemote: MessageRemote) {
        firestore.collection("messages").document(messageRemote.messageId).set(messageRemote).await()
    }

    suspend fun deleteMessage(messageId: String) {
        firestore.collection("messages").document(messageId).delete().await()
    }
}
