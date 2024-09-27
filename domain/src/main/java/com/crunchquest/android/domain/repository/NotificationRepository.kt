package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Notification

interface NotificationRepository {
    suspend fun createNotification(notification: Notification): Result<Notification>
    suspend fun getNotification(notificationId: String): Result<List<Notification>>
    suspend fun getNotificationsByUser(userId: String): Result<List<Notification>>
    suspend fun updateNotification(notification: Notification): Result<Unit>
    suspend fun deleteNotification(notificationId: String): Result<Unit>
}

