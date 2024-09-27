package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.NotificationMapper
import com.crunchquest.android.data.source.local.NotificationLocalDataSource
import com.crunchquest.android.data.source.remote.NotificationRemoteDataSource
import com.crunchquest.android.domain.entities.Notification
import com.crunchquest.android.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val remoteDataSource: NotificationRemoteDataSource,
    private val localDataSource: NotificationLocalDataSource,
    private val notificationMapper: NotificationMapper
) : NotificationRepository {

    override suspend fun createNotification(notification: com.crunchquest.android.domain.entities.Notification): Result<com.crunchquest.android.domain.entities.Notification> {
        return try {
            val notificationRemote = notificationMapper.toRemote(notification)
            remoteDataSource.createNotification(notificationRemote)
            localDataSource.saveNotification(notificationMapper.toLocal(notification)) // Cache locally
            Result.success(notification)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotification(notificationId: String): Result<List<com.crunchquest.android.domain.entities.Notification>> {
        return try {
            val notificationLocal = localDataSource.getNotification(notificationId)
            if (notificationLocal != null) {
                return Result.success(listOf(notificationMapper.fromLocal(notificationLocal)))
            }

            val notificationRemote = remoteDataSource.getNotification(notificationId)
            if (notificationRemote != null) {
                val notification = notificationMapper.fromRemote(notificationRemote)
                localDataSource.saveNotification(notificationMapper.toLocal(notification)) // Cache locally
                return Result.success(listOf(notification))
            } else {
                Result.failure(Exception("Notification not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotificationsByUser(userId: String): Result<List<com.crunchquest.android.domain.entities.Notification>> {
        return try {
            val localNotifications = localDataSource.getNotificationsByUser(userId).map { notificationMapper.fromLocal(it) }
            if (localNotifications.isNotEmpty()) {
                return Result.success(localNotifications)
            }

            val remoteNotifications = remoteDataSource.getNotificationsByUser(userId)?.map { notificationMapper.fromRemote(it) }
            if (remoteNotifications != null) {
                remoteNotifications.forEach { notification ->
                    localDataSource.saveNotification(notificationMapper.toLocal(notification)) // Cache locally
                }
                return Result.success(remoteNotifications)
            }

            Result.failure(Exception("No notifications found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNotification(notification: com.crunchquest.android.domain.entities.Notification): Result<Unit> {
        return try {
            val notificationRemote = notificationMapper.toRemote(notification)
            remoteDataSource.updateNotification(notificationRemote)
            localDataSource.updateNotification(notificationMapper.toLocal(notification))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNotification(notificationId: String): Result<Unit> {
        return try {
            val notificationLocal = localDataSource.getNotification(notificationId)
            if (notificationLocal != null) {
                remoteDataSource.deleteNotification(notificationId)
                localDataSource.deleteNotification(notificationLocal)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Notification not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}