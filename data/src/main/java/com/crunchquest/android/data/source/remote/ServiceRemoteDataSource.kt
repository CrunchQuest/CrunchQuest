package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.ServiceRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ServiceRemoteDataSource(private val firestore: FirebaseFirestore) {

    // Function to create a service
    suspend fun createService(serviceRemote: ServiceRemote): ServiceRemote {
        firestore.collection("services").document(serviceRemote.serviceId).set(serviceRemote).await()
        return serviceRemote
    }

    // Function to get a service by serviceId
    suspend fun getService(serviceId: String): ServiceRemote? {
        val serviceDocument = firestore.collection("services").document(serviceId).get().await()
        return if (serviceDocument.exists()) serviceDocument.toObject(ServiceRemote::class.java) else null
    }

    // Function to update a service
    suspend fun updateService(serviceRemote: ServiceRemote) {
        firestore.collection("services").document(serviceRemote.serviceId).set(serviceRemote).await()
    }

    // Function to delete a service
    suspend fun deleteService(serviceId: String) {
        firestore.collection("services").document(serviceId).delete().await()
    }
}
