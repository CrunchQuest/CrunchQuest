package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.ChatRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ChatRemoteDataSource(private val firestore: FirebaseFirestore) {

    suspend fun createChat(chatRemote: ChatRemote) {
        firestore.collection("chats").document(chatRemote.chatId).set(chatRemote).await()
    }

    suspend fun getChat(chatId: String): ChatRemote? {
        val chatDocument = firestore.collection("chats").document(chatId).get().await()
        return if (chatDocument.exists()) chatDocument.toObject(ChatRemote::class.java) else null
    }

    suspend fun getChatsByRequest(requestId: String): List<ChatRemote>? {
        val result = firestore.collection("chats")
            .whereEqualTo("requestId", requestId)
            .get()
            .await()
        return result.toObjects(ChatRemote::class.java)
    }

    suspend fun updateChat(chatRemote: ChatRemote) {
        firestore.collection("chats").document(chatRemote.chatId).set(chatRemote).await()
    }

    suspend fun deleteChat(chatId: String) {
        firestore.collection("chats").document(chatId).delete().await()
    }
}
