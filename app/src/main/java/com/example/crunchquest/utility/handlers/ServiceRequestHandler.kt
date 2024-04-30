package com.example.crunchquest.utility.handlers

import android.util.Log
import com.example.crunchquest.data.model.ServiceRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ServiceRequestHandler {

    var database: FirebaseDatabase
    var serviceRequestRef: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        serviceRequestRef = database.getReference("service_requests")
    }

    fun createServiceRequest(serviceRequest: ServiceRequest): Boolean {
        val id = serviceRequestRef.push().key
        serviceRequest.uid = id
        serviceRequestRef.child(id!!).setValue(serviceRequest)
        return true
    }

    fun updateServiceRequest(serviceRequest: ServiceRequest): Boolean {
        serviceRequestRef.child(serviceRequest.uid!!).setValue(serviceRequest)
        return true
    }

    fun deleteServiceRequest(serviceRequest: ServiceRequest): Boolean {
        serviceRequestRef.child(serviceRequest.uid!!).removeValue()
        return true
    }

    @ExperimentalCoroutinesApi
    fun fetchServiceRequests() = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val requests = snapshot.children.mapNotNull { it.getValue(ServiceRequest::class.java) }
                trySend(requests)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
                Log.e("ServiceRequestHandler", "Error fetching service requests: ${error.message}")
            }
        }

        serviceRequestRef.addValueEventListener(listener)

        awaitClose { serviceRequestRef.removeEventListener(listener) }
    }


}