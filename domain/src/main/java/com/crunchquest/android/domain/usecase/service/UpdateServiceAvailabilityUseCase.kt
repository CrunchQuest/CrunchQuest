package com.crunchquest.android.domain.usecase.service

import com.crunchquest.android.domain.entities.ServiceAvailability
import com.crunchquest.android.domain.repository.ServiceAvailabilityRepository
import javax.inject.Inject

class UpdateServiceAvailabilityUseCase @Inject constructor(
    private val serviceAvailabilityRepository: ServiceAvailabilityRepository
) {
    suspend operator fun invoke(serviceAvailability: ServiceAvailability): Result<Unit> {
        return serviceAvailabilityRepository.updateServiceAvailability(serviceAvailability)
    }
}
