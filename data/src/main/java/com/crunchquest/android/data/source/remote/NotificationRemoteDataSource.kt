package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.NotificationRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotificationRemoteDataSource(private val firestore: FirebaseFirestore) {

    suspend fun createNotification(notificationRemote: NotificationRemote) {
        firestore.collection("notifications").document(notificationRemote.notificationId).set(notificationRemote).await()
    }

    suspend fun getNotification(notificationId: String): NotificationRemote? {
        val notificationDocument = firestore.collection("notifications").document(notificationId).get().await()
        return if (notificationDocument.exists()) notificationDocument.toObject(NotificationRemote::class.java) else null
    }

    suspend fun getNotificationsByUser(userId: String): List<NotificationRemote>? {
        val result = firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return result.toObjects(NotificationRemote::class.java)
    }

    suspend fun updateNotification(notificationRemote: NotificationRemote) {
        firestore.collection("notifications").document(notificationRemote.notificationId).set(notificationRemote).await()
    }

    suspend fun deleteNotification(notificationId: String) {
        firestore.collection("notifications").document(notificationId).delete().await()
    }
}
