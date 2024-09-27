package com.crunchquest.android.domain.repository

import android.net.Uri
import com.crunchquest.android.domain.entities.Notification
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.entities.User

interface UserRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(user: User, password: String, idImageUri: Uri, selfieImageUri: Uri): Result<String>
    suspend fun getUser(userId: String): Result<User>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun sendVerificationEmail(): Result<Unit>
    suspend fun isUserEmailVerified(): Boolean
    suspend fun verifyCode(userId: String, code: String): Result<Unit>
    suspend fun getUserNotifications(userId: String): Result<List<Notification>>
    suspend fun updateUserInfo(user: User): Result<Unit>
    suspend fun getUserById(userId: String): Result<User>
}


