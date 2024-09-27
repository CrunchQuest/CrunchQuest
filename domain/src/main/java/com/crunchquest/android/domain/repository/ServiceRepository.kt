package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Service
import com.google.android.play.integrity.internal.c

interface ServiceRepository {
    suspend fun createService(service: Service): Result<Service>
    suspend fun getService(serviceId: String): Result<Service>
    suspend fun updateService(service: Service): Result<Unit>
    suspend fun deleteService(serviceId: String): Result<Unit>
}
