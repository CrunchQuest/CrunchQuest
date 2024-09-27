package com.crunchquest.android.domain.usecase.request

import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.repository.RequestRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class GetRequestUseCase @Inject constructor(
    private val requestRepository: RequestRepository
) {
    suspend operator fun invoke(requestId: String): Result<List<Request>> {
        return requestRepository.getRequest(requestId)
    }
}

