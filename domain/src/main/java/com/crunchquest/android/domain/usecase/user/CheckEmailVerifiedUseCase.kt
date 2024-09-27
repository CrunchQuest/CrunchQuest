package com.crunchquest.android.domain.usecase.user

import com.crunchquest.android.domain.repository.UserRepository
import javax.inject.Inject

class CheckEmailVerifiedUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Boolean {
        return userRepository.isUserEmailVerified()
    }
}