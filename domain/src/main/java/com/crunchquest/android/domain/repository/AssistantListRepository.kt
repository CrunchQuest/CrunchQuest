package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.AssistantList
import com.crunchquest.android.domain.utility.Result

interface AssistantListRepository {
    suspend fun getAssistantListByRequest(requestId: String): Result<AssistantList>
    suspend fun updateAssistantList(assistantList: AssistantList): Result<Unit>
    suspend fun createAssistantList(assistantList: AssistantList): Result<AssistantList>
    suspend fun getAssistantList(assistantListId: String): Result<AssistantList>
    suspend fun getAssistantListsByRequest(requestId: String): Result<List<AssistantList>>
    suspend fun deleteAssistantList(assistantListId: String): Result<Unit>
}
