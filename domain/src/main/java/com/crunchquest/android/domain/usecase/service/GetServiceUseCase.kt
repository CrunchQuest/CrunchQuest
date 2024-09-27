package com.crunchquest.android.domain.usecase.service

import com.crunchquest.android.domain.entities.Service
import com.crunchquest.android.domain.repository.ServiceRepository
import javax.inject.Inject

class GetServiceUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(serviceId: String): Result<Service> {
        return serviceRepository.getService(serviceId)
    }
}
