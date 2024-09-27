package com.crunchquest.android.domain.usecase.user

import com.crunchquest.android.domain.repository.UserRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class VerificationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, code: String): Result<Unit> {
        return userRepository.verifyCode(userId, code)
    }
}
