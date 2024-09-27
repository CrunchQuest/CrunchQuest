package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "services")
data class ServiceLocal(
    @PrimaryKey val serviceId: String,
    val providerId: String,
    val serviceName: String,
    val serviceDescription: String,
    val price: Int,
    val category: String,
    val status: String,
    val serviceImage: String?,
    val createdAt: String,
    val updatedAt: String
)

@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceLocal)

    @Query("SELECT * FROM services WHERE serviceId = :serviceId")
    suspend fun getService(serviceId: String): ServiceLocal?

    @Delete
    suspend fun deleteService(service: ServiceLocal)
}

class ServiceLocalDataSource(private val serviceDao: ServiceDao) {

    // Function to save a service locally
    suspend fun saveService(serviceLocal: ServiceLocal) {
        serviceDao.insertService(serviceLocal)
    }

    // Function to get a service by serviceId from local storage
    suspend fun getService(serviceId: String): ServiceLocal? {
        return serviceDao.getService(serviceId)
    }

    // Function to delete a service locally
    suspend fun deleteService(serviceLocal: ServiceLocal) {
        serviceDao.deleteService(serviceLocal)
    }
}
