package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "requests")
data class RequestLocal(
    @PrimaryKey val requestId: String,
    val requesterId: String,
    val assistantUserId: String?,
    val title: String,
    val description: String,
    val categoryId: String,
    val status: String,
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val taskSchedule: String,
    val createdAt: String,
    val updatedAt: String,
    val rewards: Double?,
    val notes: String?,
    val paymentStatus: String,
    val paymentMethod: String?,
    val requesterConfirmed: Boolean,
    val assistantConfirmed: Boolean,
    val reviewed: Boolean,
    val captureResult: String?,
    val captureResultTimestamp: String?
)

@Dao
interface RequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: RequestLocal)

    @Query("SELECT * FROM requests WHERE requestId = :requestId")
    suspend fun getRequest(requestId: String): RequestLocal?

    @Update
    suspend fun updateRequest(request: RequestLocal)

    @Delete
    suspend fun deleteRequest(request: RequestLocal)
}

class RequestLocalDataSource(private val requestDao: RequestDao) {

    // Function to save a request locally
    suspend fun saveRequest(requestLocal: RequestLocal) {
        requestDao.insertRequest(requestLocal)
    }

    // Function to get a request by requestId from local storage
    suspend fun getRequest(requestId: String): RequestLocal? {
        return requestDao.getRequest(requestId)
    }

    // Function to update a request locally
    suspend fun updateRequest(requestLocal: RequestLocal) {
        requestDao.updateRequest(requestLocal)
    }


    // Function to delete a request locally
    suspend fun deleteRequest(requestLocal: RequestLocal) {
        requestDao.deleteRequest(requestLocal)
    }
}
