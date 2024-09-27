package com.crunchquest.android.domain.usecase.request

import com.crunchquest.android.domain.repository.RequestRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class CancelRequestUseCase @Inject constructor(
    private val requestRepository: RequestRepository
) {
    suspend operator fun invoke(requestId: String): Result<Unit> {
        return try {
            requestRepository.cancelRequest(requestId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}