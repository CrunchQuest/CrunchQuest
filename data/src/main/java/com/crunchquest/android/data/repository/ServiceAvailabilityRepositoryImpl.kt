package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.ServiceAvailabilityMapper
import com.crunchquest.android.data.source.local.ServiceAvailabilityLocalDataSource
import com.crunchquest.android.data.source.remote.ServiceAvailabilityRemoteDataSource
import com.crunchquest.android.domain.entities.ServiceAvailability
import com.crunchquest.android.domain.repository.ServiceAvailabilityRepository

class ServiceAvailabilityRepositoryImpl(
    private val remoteDataSource: ServiceAvailabilityRemoteDataSource,
    private val localDataSource: ServiceAvailabilityLocalDataSource,
    private val serviceAvailabilityMapper: ServiceAvailabilityMapper
) : ServiceAvailabilityRepository {

    override suspend fun createServiceAvailability(availability: com.crunchquest.android.domain.entities.ServiceAvailability): Result<com.crunchquest.android.domain.entities.ServiceAvailability> {
        return try {
            val availabilityRemote = serviceAvailabilityMapper.toRemote(availability)
            remoteDataSource.createServiceAvailability(availabilityRemote)
            localDataSource.saveServiceAvailability(serviceAvailabilityMapper.toLocal(availability)) // Cache locally
            Result.success(availability)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getServiceAvailability(serviceId: String): Result<List<com.crunchquest.android.domain.entities.ServiceAvailability>> {
        return try {
            val availabilityLocal = localDataSource.getServiceAvailability(serviceId)
            if (availabilityLocal != null) {
                return Result.success(listOf(serviceAvailabilityMapper.fromLocal(availabilityLocal)))
            }

            val availabilityRemote = remoteDataSource.getServiceAvailability(serviceId)
            if (availabilityRemote != null) {
                val availability = serviceAvailabilityMapper.fromRemote(availabilityRemote)
                localDataSource.saveServiceAvailability(serviceAvailabilityMapper.toLocal(availability)) // Cache locally
                return Result.success(listOf(availability))
            } else {
                Result.failure(Exception("Service availability not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAvailabilityByService(serviceId: String): Result<List<com.crunchquest.android.domain.entities.ServiceAvailability>> {
        return try {
            val localAvailabilities = localDataSource.getAvailabilityByService(serviceId).map { serviceAvailabilityMapper.fromLocal(it) }
            if (localAvailabilities.isNotEmpty()) {
                return Result.success(localAvailabilities)
            }

            val remoteAvailabilities = remoteDataSource.getAvailabilityByService(serviceId)?.map { serviceAvailabilityMapper.fromRemote(it) }
            if (remoteAvailabilities != null) {
                remoteAvailabilities.forEach { availability ->
                    localDataSource.saveServiceAvailability(serviceAvailabilityMapper.toLocal(availability)) // Cache locally
                }
                return Result.success(remoteAvailabilities)
            }

            Result.failure(Exception("No service availabilities found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateServiceAvailability(availability: com.crunchquest.android.domain.entities.ServiceAvailability): Result<Unit> {
        return try {
            val availabilityRemote = serviceAvailabilityMapper.toRemote(availability)
            remoteDataSource.updateServiceAvailability(availabilityRemote)
            localDataSource.updateServiceAvailability(serviceAvailabilityMapper.toLocal(availability))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteServiceAvailability(availabilityId: String): Result<Unit> {
        return try {
            val availabilityLocal = localDataSource.getServiceAvailability(availabilityId)
            if (availabilityLocal != null) {
                remoteDataSource.deleteServiceAvailability(availabilityId)
                localDataSource.deleteServiceAvailability(availabilityLocal)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Service availability not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
