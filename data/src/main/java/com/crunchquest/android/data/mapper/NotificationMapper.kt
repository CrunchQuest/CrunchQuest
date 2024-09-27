package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.NotificationRemote
import com.crunchquest.android.data.source.local.NotificationLocal
import com.crunchquest.android.domain.entities.Notification
import javax.inject.Inject

class NotificationMapper @Inject constructor() {

    // Convert remote data model to domain entity
    fun fromRemote(notificationRemote: NotificationRemote): Notification {
        return Notification(
            notificationId = notificationRemote.notificationId,
            userId = notificationRemote.userId,
            type = notificationRemote.type,
            title = notificationRemote.title,
            message = notificationRemote.message,
            createdAt = notificationRemote.createdAt,
            status = notificationRemote.status
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(notification: Notification): NotificationRemote {
        return NotificationRemote(
            notificationId = notification.notificationId,
            userId = notification.userId,
            type = notification.type,
            title = notification.title,
            message = notification.message,
            createdAt = notification.createdAt,
            status = notification.status
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(notificationLocal: NotificationLocal): Notification {
        return Notification(
            notificationId = notificationLocal.notificationId,
            userId = notificationLocal.userId,
            type = notificationLocal.type,
            title = notificationLocal.title,
            message = notificationLocal.message,
            createdAt = notificationLocal.createdAt,
            status = notificationLocal.status
        )
    }

    // Convert domain entity to local data model
    fun toLocal(notification: Notification): NotificationLocal {
        return NotificationLocal(
            notificationId = notification.notificationId,
            userId = notification.userId,
            type = notification.type,
            title = notification.title,
            message = notification.message,
            createdAt = notification.createdAt,
            status = notification.status
        )
    }
}


