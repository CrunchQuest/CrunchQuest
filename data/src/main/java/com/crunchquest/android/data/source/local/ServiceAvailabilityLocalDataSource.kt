package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "service_availability")
data class ServiceAvailabilityLocal(
    @PrimaryKey val availabilityId: String,
    val serviceId: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

@Dao
interface ServiceAvailabilityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceAvailability(availability: ServiceAvailabilityLocal)

    @Query("SELECT * FROM service_availability WHERE availabilityId = :availabilityId")
    suspend fun getServiceAvailability(availabilityId: String): ServiceAvailabilityLocal?

    @Query("SELECT * FROM service_availability WHERE serviceId = :serviceId")
    suspend fun getAvailabilityByService(serviceId: String): List<ServiceAvailabilityLocal>

    @Update
    suspend fun updateServiceAvailability(availability: ServiceAvailabilityLocal)

    @Delete
    suspend fun deleteServiceAvailability(availability: ServiceAvailabilityLocal)
}

class ServiceAvailabilityLocalDataSource(private val serviceAvailabilityDao: ServiceAvailabilityDao) {

    suspend fun saveServiceAvailability(availabilityLocal: ServiceAvailabilityLocal) {
        serviceAvailabilityDao.insertServiceAvailability(availabilityLocal)
    }

    suspend fun getServiceAvailability(availabilityId: String): ServiceAvailabilityLocal? {
        return serviceAvailabilityDao.getServiceAvailability(availabilityId)
    }

    suspend fun getAvailabilityByService(serviceId: String): List<ServiceAvailabilityLocal> {
        return serviceAvailabilityDao.getAvailabilityByService(serviceId)
    }

    suspend fun updateServiceAvailability(availabilityLocal: ServiceAvailabilityLocal) {
        serviceAvailabilityDao.updateServiceAvailability(availabilityLocal)
    }

    suspend fun deleteServiceAvailability(availabilityLocal: ServiceAvailabilityLocal) {
        serviceAvailabilityDao.deleteServiceAvailability(availabilityLocal)
    }
}
