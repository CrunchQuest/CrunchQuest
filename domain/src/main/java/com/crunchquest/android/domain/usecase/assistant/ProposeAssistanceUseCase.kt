package com.crunchquest.android.domain.usecase.assistant

import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.android.domain.repository.AssistantRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class ProposeAssistanceUseCase @Inject constructor(
    private val assistantRepository: AssistantRepository
) {
    suspend operator fun invoke(assistant: Assistant): Result<Assistant> {
        return assistantRepository.proposeAssistance(assistant)
    }
}
