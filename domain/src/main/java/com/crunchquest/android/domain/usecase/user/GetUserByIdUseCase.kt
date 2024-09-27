package com.crunchquest.android.domain.usecase.user

import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User> {
        return userRepository.getUserById(userId) // Ensure getUserById in UserRepository returns Result<User>
    }
}
