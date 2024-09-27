package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.ProviderMapper
import com.crunchquest.android.data.source.local.ProviderLocalDataSource
import com.crunchquest.android.data.source.remote.ProviderRemoteDataSource
import com.crunchquest.android.domain.entities.Provider
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.repository.ProviderRepository

class ProviderRepositoryImpl(
    private val remoteDataSource: ProviderRemoteDataSource,
    private val localDataSource: ProviderLocalDataSource,
    private val providerMapper: ProviderMapper
) : ProviderRepository {

    override suspend fun createProvider(provider: Provider): Result<Provider> {
        return try {
            val providerRemote = providerMapper.toRemote(provider)
            remoteDataSource.createProvider(providerRemote)
            localDataSource.saveProvider(providerMapper.toLocal(provider)) // Cache locally
            Result.Success(provider)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getProvider(providerId: String): Result<List<Provider>> {
        return try {
            val providerLocal = localDataSource.getProvider(providerId)
            if (providerLocal != null) {
                return Result.Success(listOf(providerMapper.fromLocal(providerLocal)))
            }

            val providerRemote = remoteDataSource.getProvider(providerId)
            if (providerRemote != null) {
                val provider = providerMapper.fromRemote(providerRemote)
                localDataSource.saveProvider(providerMapper.toLocal(provider)) // Cache locally
                Result.Success(listOf(provider))
            } else {
                Result.Error(Exception("Provider not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateProvider(provider: Provider): Result<Unit> {
        return try {
            val providerRemote = providerMapper.toRemote(provider)
            remoteDataSource.updateProvider(providerRemote)
            localDataSource.updateProvider(providerMapper.toLocal(provider))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteProvider(providerId: String): Result<Unit> {
        return try {
            val providerLocal = localDataSource.getProvider(providerId)
            if (providerLocal != null) {
                remoteDataSource.deleteProvider(providerId)
                localDataSource.deleteProvider(providerLocal)
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Provider not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
