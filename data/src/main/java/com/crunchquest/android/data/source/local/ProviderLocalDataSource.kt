package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "providers")
data class ProviderLocal(
    @PrimaryKey val providerId: String,
    val userId: String,
    val credentialsUrl: String?,
    val licenses: String?,
    val serviceArea: String,
    val paymentDetails: String?,
    val termsAccepted: Boolean,
    val termsAcceptedDate: String,
    val rating: Double,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

@Dao
interface ProviderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: ProviderLocal)

    @Query("SELECT * FROM providers WHERE providerId = :providerId")
    suspend fun getProvider(providerId: String): ProviderLocal?

    @Update
    suspend fun updateProvider(provider: ProviderLocal)

    @Delete
    suspend fun deleteProvider(provider: ProviderLocal)
}

class ProviderLocalDataSource(private val providerDao: ProviderDao) {

    suspend fun saveProvider(providerLocal: ProviderLocal) {
        providerDao.insertProvider(providerLocal)
    }

    suspend fun getProvider(providerId: String): ProviderLocal? {
        return providerDao.getProvider(providerId)
    }

    suspend fun updateProvider(providerLocal: ProviderLocal) {
        providerDao.updateProvider(providerLocal)
    }

    suspend fun deleteProvider(providerLocal: ProviderLocal) {
        providerDao.deleteProvider(providerLocal)
    }
}
