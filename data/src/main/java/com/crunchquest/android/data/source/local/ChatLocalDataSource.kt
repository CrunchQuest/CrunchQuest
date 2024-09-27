package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "chats")
data class ChatLocal(
    @PrimaryKey val chatId: String,
    val requestId: String,
    val members: List<String>
)

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatLocal>)

    @Query("SELECT * FROM chats WHERE chatId = :chatId")
    suspend fun getChat(chatId: String): ChatLocal?

    @Query("SELECT * FROM chats WHERE requestId = :requestId")
    suspend fun getChatsByRequest(requestId: String): List<ChatLocal>

    @Update
    suspend fun updateChat(chat: ChatLocal)

    @Delete
    suspend fun deleteChat(chat: ChatLocal)
}

class ChatLocalDataSource(private val chatDao: ChatDao) {

    suspend fun saveChat(chatLocal: ChatLocal) {
        chatDao.insertChat(chatLocal)
    }

    suspend fun saveChats(chatLocals: List<ChatLocal>) {
        chatDao.insertChats(chatLocals)
    }

    suspend fun getChat(chatId: String): ChatLocal? {
        return chatDao.getChat(chatId)
    }

    suspend fun getChatsByRequest(requestId: String): List<ChatLocal> {
        return chatDao.getChatsByRequest(requestId)
    }

    suspend fun updateChat(chatLocal: ChatLocal) {
        chatDao.updateChat(chatLocal)
    }

    suspend fun deleteChat(chatLocal: ChatLocal) {
        chatDao.deleteChat(chatLocal)
    }
}

