package com.crunchquest.android.domain.usecase.service

import com.crunchquest.android.domain.entities.ServiceAvailability
import com.crunchquest.android.domain.repository.ServiceAvailabilityRepository
import javax.inject.Inject

class GetServiceAvailabilityUseCase @Inject constructor(
    private val serviceAvailabilityRepository: ServiceAvailabilityRepository
) {
    suspend operator fun invoke(serviceId: String): Result<List<ServiceAvailability>> {
        return serviceAvailabilityRepository.getServiceAvailability(serviceId)
    }
}
