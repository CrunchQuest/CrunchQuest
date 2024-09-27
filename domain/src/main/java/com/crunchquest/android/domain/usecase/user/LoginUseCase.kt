package com.crunchquest.android.domain.usecase.user

import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.repository.UserRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return userRepository.login(email, password)
    }
}


