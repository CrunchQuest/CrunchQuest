package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "messages")
data class MessageLocal(
    @PrimaryKey val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: String
)

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageLocal)

    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessage(messageId: String): MessageLocal?

    @Query("SELECT * FROM messages WHERE chatId = :chatId")
    suspend fun getMessagesByChat(chatId: String): List<MessageLocal>

    @Update
    suspend fun updateMessage(message: MessageLocal)

    @Delete
    suspend fun deleteMessage(message: MessageLocal)
}

class MessageLocalDataSource(private val messageDao: MessageDao) {

    suspend fun saveMessage(messageLocal: MessageLocal) {
        messageDao.insertMessage(messageLocal)
    }

    suspend fun getMessage(messageId: String): MessageLocal? {
        return messageDao.getMessage(messageId)
    }

    suspend fun getMessagesByChat(chatId: String): List<MessageLocal> {
        return messageDao.getMessagesByChat(chatId)
    }

    suspend fun updateMessage(messageLocal: MessageLocal) {
        messageDao.updateMessage(messageLocal)
    }

    suspend fun deleteMessage(messageLocal: MessageLocal) {
        messageDao.deleteMessage(messageLocal)
    }
}
