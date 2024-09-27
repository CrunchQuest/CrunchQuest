package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.ServiceAvailabilityRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ServiceAvailabilityRemoteDataSource(private val firestore: FirebaseFirestore) {

    suspend fun createServiceAvailability(availabilityRemote: ServiceAvailabilityRemote) {
        firestore.collection("service_availability").document(availabilityRemote.availabilityId).set(availabilityRemote).await()
    }

    suspend fun getServiceAvailability(availabilityId: String): ServiceAvailabilityRemote? {
        val availabilityDocument = firestore.collection("service_availability").document(availabilityId).get().await()
        return if (availabilityDocument.exists()) availabilityDocument.toObject(
            ServiceAvailabilityRemote::class.java) else null
    }

    suspend fun getAvailabilityByService(serviceId: String): List<ServiceAvailabilityRemote>? {
        val result = firestore.collection("service_availability")
            .whereEqualTo("serviceId", serviceId)
            .get()
            .await()
        return result.toObjects(ServiceAvailabilityRemote::class.java)
    }

    suspend fun updateServiceAvailability(availabilityRemote: ServiceAvailabilityRemote) {
        firestore.collection("service_availability").document(availabilityRemote.availabilityId).set(availabilityRemote).await()
    }

    suspend fun deleteServiceAvailability(availabilityId: String) {
        firestore.collection("service_availability").document(availabilityId).delete().await()
    }
}
