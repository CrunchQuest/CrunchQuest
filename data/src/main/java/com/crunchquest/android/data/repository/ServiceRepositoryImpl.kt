package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.ServiceMapper
import com.crunchquest.android.data.source.local.ServiceLocal
import com.crunchquest.android.data.source.local.ServiceLocalDataSource
import com.crunchquest.android.data.source.remote.ServiceRemoteDataSource
import com.crunchquest.android.domain.entities.Service
import com.crunchquest.android.domain.repository.ServiceRepository

class ServiceRepositoryImpl(
    private val remoteDataSource: ServiceRemoteDataSource,
    private val localDataSource: ServiceLocalDataSource,
    private val serviceMapper: ServiceMapper
) : ServiceRepository {

    // Create a service
    override suspend fun createService(service: Service): Result<Service> {
        return try {
            val serviceRemote = serviceMapper.toRemote(service)
            val createdServiceRemote = remoteDataSource.createService(serviceRemote)
            val createdService = serviceMapper.fromRemote(createdServiceRemote)
            localDataSource.saveService(serviceMapper.toLocal(createdService)) // Cache locally
            Result.success(createdService)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get a service by serviceId
    override suspend fun getService(serviceId: String): Result<Service> {
        return try {
            val serviceLocal = localDataSource.getService(serviceId)
            if (serviceLocal != null) {
                return Result.success(serviceMapper.fromLocal(serviceLocal))
            }

            val serviceRemote = remoteDataSource.getService(serviceId)
            if (serviceRemote != null) {
                val service = serviceMapper.fromRemote(serviceRemote)
                localDataSource.saveService(serviceMapper.toLocal(service)) // Cache locally
                Result.success(service)
            } else {
                Result.failure(Exception("Service not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update a service
    override suspend fun updateService(service: Service): Result<Unit> {
        return try {
            val serviceRemote = serviceMapper.toRemote(service)
            remoteDataSource.updateService(serviceRemote)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete a service
    override suspend fun deleteService(serviceId: String): Result<Unit> {
        return try {
            val serviceLocal = localDataSource.getService(serviceId)
            if (serviceLocal != null) {
                remoteDataSource.deleteService(serviceId)
                localDataSource.deleteService(serviceLocal) // Remove from local cache
                Result.success(Unit)
            } else {
                Result.failure(Exception("Service not found in local storage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
