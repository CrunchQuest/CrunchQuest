package com.crunchquest.android.domain.usecase.assistant

import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.android.domain.repository.AssistantRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class GetAssistantsByRequestUseCase @Inject constructor(
    private val assistantRepository: AssistantRepository
) {
    suspend operator fun invoke(requestId: String): Result<List<Assistant>> {
        return assistantRepository.getAssistantsByRequest(requestId)
    }
}
