package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.ProviderRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProviderRemoteDataSource(private val firestore: FirebaseFirestore) {

    suspend fun createProvider(providerRemote: ProviderRemote) {
        firestore.collection("providers").document(providerRemote.providerId).set(providerRemote).await()
    }

    suspend fun getProvider(providerId: String): ProviderRemote? {
        val providerDocument = firestore.collection("providers").document(providerId).get().await()
        return if (providerDocument.exists()) providerDocument.toObject(ProviderRemote::class.java) else null
    }

    suspend fun updateProvider(providerRemote: ProviderRemote) {
        firestore.collection("providers").document(providerRemote.providerId).set(providerRemote).await()
    }

    suspend fun deleteProvider(providerId: String) {
        firestore.collection("providers").document(providerId).delete().await()
    }
}
