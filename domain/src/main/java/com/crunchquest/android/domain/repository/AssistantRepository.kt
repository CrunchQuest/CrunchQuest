package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.android.domain.utility.Result

interface AssistantRepository {
    suspend fun proposeAssistance(assistant: Assistant): Result<Assistant>
    suspend fun getAssistantsByRequest(requestId: String): Result<List<Assistant>>
    suspend fun getAssistantsByStatus(status: String): Result<List<Assistant>>
    suspend fun updateAssistant(assistant: Assistant): Result<Unit>
    suspend fun deleteAssistant(assistantId: String): Result<Unit>
}

