package com.crunchquest.android.domain.usecase.provider

import com.crunchquest.android.domain.entities.Provider
import com.crunchquest.android.domain.repository.ProviderRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class CreateProviderUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    suspend operator fun invoke(provider: Provider): Result<Provider> {
        return providerRepository.createProvider(provider)
    }
}
