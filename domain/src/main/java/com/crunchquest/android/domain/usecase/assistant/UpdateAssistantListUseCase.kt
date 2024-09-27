package com.crunchquest.android.domain.usecase.assistant

import com.crunchquest.android.domain.entities.AssistantList
import com.crunchquest.android.domain.repository.AssistantListRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class UpdateAssistantListUseCase @Inject constructor(
    private val assistantListRepository: AssistantListRepository
) {
    suspend operator fun invoke(assistantList: AssistantList): Result<Unit> {
        return assistantListRepository.updateAssistantList(assistantList)
    }
}
