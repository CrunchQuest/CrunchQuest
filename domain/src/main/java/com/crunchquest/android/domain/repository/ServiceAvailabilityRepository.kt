package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.ServiceAvailability

interface ServiceAvailabilityRepository {
    suspend fun getServiceAvailability(serviceId: String): Result<List<ServiceAvailability>>
    suspend fun createServiceAvailability(availability: ServiceAvailability): Result<ServiceAvailability>
    suspend fun getAvailabilityByService(serviceId: String): Result<List<ServiceAvailability>>
    suspend fun updateServiceAvailability(availability: ServiceAvailability): Result<Unit>
    suspend fun deleteServiceAvailability(availabilityId: String): Result<Unit>
}
