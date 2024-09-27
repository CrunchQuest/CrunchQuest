package com.crunchquest.android.domain.usecase.assistant

import com.crunchquest.android.domain.entities.AssistantList
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.repository.AssistantListRepository
import javax.inject.Inject

class GetAssistantListByRequestUseCase @Inject constructor(
    private val assistantListRepository: AssistantListRepository
) {
    suspend operator fun invoke(requestId: String): Result<AssistantList> {
        return assistantListRepository.getAssistantListByRequest(requestId)
    }
}
