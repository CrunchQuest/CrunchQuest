package com.crunchquest.android.domain.usecase.user

import android.net.Uri
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.repository.UserRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User, password: String, idImageUri: Uri, selfieImageUri: Uri): Result<String> {
        return userRepository.register(user, password, idImageUri, selfieImageUri)
    }
}



