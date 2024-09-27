package com.crunchquest.android.domain.usecase.user

import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.repository.UserRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User> {
        return userRepository.getUser(userId)
    }
}
