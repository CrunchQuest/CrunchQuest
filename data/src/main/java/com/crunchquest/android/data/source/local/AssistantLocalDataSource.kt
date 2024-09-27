package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "assistant")
data class AssistantLocal(
    @PrimaryKey val assistantId: String,
    val requestId: String,
    val assistantUserId: String,
    val assistanceStatus: String,
    val proposedRewards: Double?,
    val notes: String?,
    val taskAvailability: String,
    val assistConfirmed: Boolean,
    val paymentRequested: Boolean,
    val startedExecuting: Boolean,
    val createdAt: String,
    val updatedAt: String
)

@Dao
interface AssistantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssistant(assistant: AssistantLocal)

    @Query("SELECT * FROM assistant WHERE requestId = :requestId")
    suspend fun getAssistantsByRequest(requestId: String): List<AssistantLocal>

    @Update
    suspend fun updateAssistant(assistant: AssistantLocal)

    @Delete
    suspend fun deleteAssistant(assistant: AssistantLocal)
}

class AssistantLocalDataSource(private val assistantDao: AssistantDao) {

    suspend fun saveAssistant(assistantLocal: AssistantLocal) {
        assistantDao.insertAssistant(assistantLocal)
    }

    suspend fun getAssistantsByRequest(requestId: String): List<AssistantLocal> {
        return assistantDao.getAssistantsByRequest(requestId)
    }

    suspend fun updateAssistant(assistantLocal: AssistantLocal) {
        assistantDao.updateAssistant(assistantLocal)
    }

    suspend fun deleteAssistant(assistantLocal: AssistantLocal) {
        assistantDao.deleteAssistant(assistantLocal)
    }
}



