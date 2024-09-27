package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.AssistantRemote
import com.crunchquest.android.data.remote.api.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AssistantRemoteDataSource(private val apiService: ApiService) {

    // Function to propose assistance on the backend
    suspend fun proposeAssistance(assistantRemote: AssistantRemote): AssistantRemote {
        val response = apiService.createAssist(assistantRemote)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to propose assistance: ${response.message()}")
        }
    }

    // Function to get assistants by requestId
    suspend fun getAssistantsByRequest(requestId: String): List<AssistantRemote> {
        val response = apiService.getAssist(requestId)
        if (response.isSuccessful) {
            return response.body()?.let { listOf(it) } ?: emptyList() // Convert to list if body is not null
        } else {
            throw Exception("Failed to fetch assistants: ${response.message()}")
        }
    }

    suspend fun getAssistantsByStatus(status: String): List<AssistantRemote> {
        val response = apiService.getAssists(status)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch assists: ${response.message()}")
        }
    }

    // Function to update an existing assistant
    suspend fun updateAssistant(assistantRemote: AssistantRemote) {
        val response = apiService.updateAssist(assistantRemote.assistantId, assistantRemote)
        if (!response.isSuccessful) {
            throw Exception("Failed to update assistant: ${response.message()}")
        }
    }

    // Function to delete an assistant by assistantId
    suspend fun deleteAssistant(assistantId: String) {
        val response = apiService.deleteAssist(assistantId)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete assistant: ${response.message()}")
        }
    }
}


