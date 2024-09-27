package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.AssistantListRemote
import com.crunchquest.android.data.remote.api.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AssistantListRemoteDataSource(private val apiService: ApiService) {

    suspend fun createAssistantList(assistantList: AssistantListRemote) {
//        apiService.createAssistantList(assistantList)
    }

    suspend fun getAssistantList(assistantListId: String) {
//        return apiService.getAssistantList(assistantListId)
    }

    suspend fun getAssistantListsByRequest(requestId: String) {
//        return apiService.getAssistantListsByRequest(requestId)
    }

    suspend fun updateAssistantList(assistantList: AssistantListRemote) {
//        apiService.updateAssistantList(assistantList.id, assistantList)
    }

    suspend fun deleteAssistantList(assistantListId: String) {
//        apiService.deleteAssistantList(assistantListId)
    }
}