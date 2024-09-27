package com.crunchquest.android.data.source.remote

import android.util.Log
import com.crunchquest.android.data.model.remote.RequestRemote
import com.crunchquest.android.data.remote.api.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RequestRemoteDataSource(private val apiService: ApiService) {

    // Function to create a request on the backend
    suspend fun createRequest(requestRemote: RequestRemote): RequestRemote {
        val response = apiService.createRequest(requestRemote)
        if (response.isSuccessful) {
            Log.d("RequestRemoteDataSource", "Request sent with rewards: ${requestRemote.rewards}")
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to create request: ${response.message()}")
        }
    }


    // Function to get a request by requestId
    suspend fun getRequest(requestId: String): RequestRemote? {
        val response = apiService.getRequest(requestId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getRequestDetails(requestId: String): RequestRemote {
        val response = apiService.getRequestDetails(requestId)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Request details not found")
        } else {
            throw Exception("Failed to fetch request details: ${response.message()}")
        }
    }

    // Function to get requests by status
    suspend fun getRequestsByStatus(status: String): List<RequestRemote> {
        val response = apiService.getRequests(status)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch requests: ${response.message()}")
        }
    }

    // Function to update an existing request
    suspend fun updateRequest(requestRemote: RequestRemote) {
        val response = apiService.updateRequest(requestRemote.requestId, requestRemote)
        if (!response.isSuccessful) {
            throw Exception("Failed to update request: ${response.message()}")
        }
    }

    suspend fun getFeaturedRequests(): List<RequestRemote> {
        val response = apiService.getFeaturedRequests()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch featured requests: ${response.message()}")
        }
    }

    // Function to delete a request by requestId
    suspend fun deleteRequest(requestId: String) {
        val response = apiService.deleteRequest(requestId)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete request: ${response.message()}")
        }
    }

    suspend fun cancelRequest(requestId: String) {
        val response = apiService.cancelRequest(requestId)
        if (!response.isSuccessful) {
            throw Exception("Failed to cancel request: ${response.message()}")
        }
    }
}
