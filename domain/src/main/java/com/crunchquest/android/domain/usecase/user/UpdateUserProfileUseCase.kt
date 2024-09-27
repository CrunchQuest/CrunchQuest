package com.crunchquest.android.domain.usecase.user

import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.repository.UserRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return userRepository.updateUser(user)
    }
}
