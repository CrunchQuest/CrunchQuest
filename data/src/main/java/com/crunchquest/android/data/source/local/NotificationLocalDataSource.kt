package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "notifications")
data class NotificationLocal(
    @PrimaryKey val notificationId: String,
    val userId: String,
    val type: String,
    val title: String,
    val message: String,
    val createdAt: String,
    val status: String
)

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationLocal)

    @Query("SELECT * FROM notifications WHERE notificationId = :notificationId")
    suspend fun getNotification(notificationId: String): NotificationLocal?

    @Query("SELECT * FROM notifications WHERE userId = :userId")
    suspend fun getNotificationsByUser(userId: String): List<NotificationLocal>

    @Update
    suspend fun updateNotification(notification: NotificationLocal)

    @Delete
    suspend fun deleteNotification(notification: NotificationLocal)
}

class NotificationLocalDataSource(private val notificationDao: NotificationDao) {

    suspend fun saveNotification(notificationLocal: NotificationLocal) {
        notificationDao.insertNotification(notificationLocal)
    }

    suspend fun getNotification(notificationId: String): NotificationLocal? {
        return notificationDao.getNotification(notificationId)
    }

    suspend fun getNotificationsByUser(userId: String): List<NotificationLocal> {
        return notificationDao.getNotificationsByUser(userId)
    }

    suspend fun updateNotification(notificationLocal: NotificationLocal) {
        notificationDao.updateNotification(notificationLocal)
    }

    suspend fun deleteNotification(notificationLocal: NotificationLocal) {
        notificationDao.deleteNotification(notificationLocal)
    }
}
