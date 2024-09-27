package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Provider
import com.crunchquest.android.domain.utility.Result

interface ProviderRepository {
    suspend fun createProvider(provider: Provider): Result<Provider>
    suspend fun getProvider(providerId: String): Result<List<Provider>>
    suspend fun updateProvider(provider: Provider): Result<Unit>
    suspend fun deleteProvider(providerId: String): Result<Unit>
}

