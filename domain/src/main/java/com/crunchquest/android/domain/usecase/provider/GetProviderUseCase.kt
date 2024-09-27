package com.crunchquest.android.domain.usecase.provider

import com.crunchquest.android.domain.entities.Provider
import com.crunchquest.android.domain.repository.ProviderRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class GetProviderUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    suspend operator fun invoke(providerId: String): Result<List<Provider>> {
        return providerRepository.getProvider(providerId)
    }
}
