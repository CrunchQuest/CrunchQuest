package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.ProviderRemote
import com.crunchquest.android.data.source.local.ProviderLocal
import com.crunchquest.android.domain.entities.Provider
import javax.inject.Inject

class ProviderMapper @Inject constructor() {

    // Convert remote data model to domain entity
    fun fromRemote(providerRemote: ProviderRemote): Provider {
        return Provider(
            providerId = providerRemote.providerId,
            userId = providerRemote.userId,
            credentialsUrl = providerRemote.credentialsUrl,
            licenses = providerRemote.licenses,
            serviceArea = providerRemote.serviceArea,
            paymentDetails = providerRemote.paymentDetails,
            termsAccepted = providerRemote.termsAccepted,
            termsAcceptedDate = providerRemote.termsAcceptedDate,
            rating = providerRemote.rating,
            status = providerRemote.status,
            createdAt = providerRemote.createdAt,
            updatedAt = providerRemote.updatedAt
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(provider: Provider): ProviderRemote {
        return ProviderRemote(
            providerId = provider.providerId,
            userId = provider.userId,
            credentialsUrl = provider.credentialsUrl,
            licenses = provider.licenses,
            serviceArea = provider.serviceArea,
            paymentDetails = provider.paymentDetails,
            termsAccepted = provider.termsAccepted,
            termsAcceptedDate = provider.termsAcceptedDate,
            rating = provider.rating,
            status = provider.status,
            createdAt = provider.createdAt,
            updatedAt = provider.updatedAt
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(providerLocal: ProviderLocal): Provider {
        return Provider(
            providerId = providerLocal.providerId,
            userId = providerLocal.userId,
            credentialsUrl = providerLocal.credentialsUrl,
            licenses = providerLocal.licenses,
            serviceArea = providerLocal.serviceArea,
            paymentDetails = providerLocal.paymentDetails,
            termsAccepted = providerLocal.termsAccepted,
            termsAcceptedDate = providerLocal.termsAcceptedDate,
            rating = providerLocal.rating,
            status = providerLocal.status,
            createdAt = providerLocal.createdAt,
            updatedAt = providerLocal.updatedAt
        )
    }

    // Convert domain entity to local data model
    fun toLocal(provider: Provider): ProviderLocal {
        return ProviderLocal(
            providerId = provider.providerId,
            userId = provider.userId,
            credentialsUrl = provider.credentialsUrl,
            licenses = provider.licenses,
            serviceArea = provider.serviceArea,
            paymentDetails = provider.paymentDetails,
            termsAccepted = provider.termsAccepted,
            termsAcceptedDate = provider.termsAcceptedDate,
            rating = provider.rating,
            status = provider.status,
            createdAt = provider.createdAt,
            updatedAt = provider.updatedAt
        )
    }
}
