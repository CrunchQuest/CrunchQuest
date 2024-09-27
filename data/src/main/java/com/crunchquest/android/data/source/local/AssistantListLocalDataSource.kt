package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "assistant_list")
data class AssistantListLocal(
    @PrimaryKey val assistantListId: String,
    val requestId: String,
    val arrayAssistantUserId: List<String>,
    val assistanceStatus: String,
    val isSelectedByRequester: Boolean,
    val createdAt: String,
    val updatedAt: String
)

@Dao
interface AssistantListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssistantList(assistantList: AssistantListLocal)

    @Query("SELECT * FROM assistant_list WHERE assistantListId = :assistantListId")
    suspend fun getAssistantList(assistantListId: String): AssistantListLocal?

    @Query("SELECT * FROM assistant_list WHERE requestId = :requestId")
    suspend fun getAssistantListsByRequest(requestId: String): List<AssistantListLocal>

    @Update
    suspend fun updateAssistantList(assistantList: AssistantListLocal)

    @Delete
    suspend fun deleteAssistantList(assistantList: AssistantListLocal)
}

class AssistantListLocalDataSource(private val assistantListDao: AssistantListDao) {

    suspend fun saveAssistantList(assistantListLocal: AssistantListLocal) {
        assistantListDao.insertAssistantList(assistantListLocal)
    }

    suspend fun getAssistantList(assistantListId: String): AssistantListLocal? {
        return assistantListDao.getAssistantList(assistantListId)
    }

    suspend fun getAssistantListsByRequest(requestId: String): List<AssistantListLocal> {
        return assistantListDao.getAssistantListsByRequest(requestId)
    }

    suspend fun updateAssistantList(assistantListLocal: AssistantListLocal) {
        assistantListDao.updateAssistantList(assistantListLocal)
    }

    suspend fun deleteAssistantList(assistantListLocal: AssistantListLocal) {
        assistantListDao.deleteAssistantList(assistantListLocal)
    }
}
