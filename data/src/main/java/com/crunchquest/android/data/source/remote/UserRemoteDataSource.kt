package com.crunchquest.android.data.source.remote

import android.net.Uri
import com.crunchquest.android.data.mapper.UserMapper
import com.crunchquest.android.data.model.remote.UserRemote
import com.crunchquest.android.data.remote.api.ApiService
import com.crunchquest.android.data.remote.dto.LoginRequest
import com.crunchquest.android.data.remote.dto.RegisterRequest
import com.crunchquest.android.data.remote.dto.VerificationRequest
import com.crunchquest.android.data.remote.model.NotificationResponse
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class UserRemoteDataSource(private val apiService: ApiService) {

    suspend fun login(email: String, password: String): UserRemote {
        val response = apiService.login(LoginRequest(email, password))
        if (response.isSuccessful) {
            val loginResponse = response.body()
            loginResponse?.user?.let {
                return it
            } ?: throw Exception("Invalid response from server")
        } else {
            throw Exception("Login failed: ${response.message()}")
        }
    }

    suspend fun register(user: UserRemote, password: String, idImageUri: Uri, selfieImageUri: Uri): String {
        val registerRequest = RegisterRequest(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            password = password
        )
        val response = apiService.register(registerRequest)
        if (response.isSuccessful) {
            val userResponse = response.body()?.user
            userResponse?.let {
                return it.userId
            } ?: throw Exception("Invalid response from server")
        } else {
            throw Exception("Registration failed: ${response.message()}")
        }
    }

    suspend fun getUser(userId: String): UserRemote {
        val response = apiService.getUser(userId)
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            } ?: throw Exception("User not found")
        } else {
            throw Exception("Failed to fetch user: ${response.message()}")
        }
    }

    suspend fun getUserById(userId: String): UserRemote {
        val response = apiService.getUser(userId)
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            } ?: throw Exception("User not found")
        } else {
            throw Exception("Failed to fetch user by ID: ${response.message()}")
        }
    }

    suspend fun updateUser(user: UserRemote) {
        val response = apiService.updateUser(user.userId, user)
        if (!response.isSuccessful) {
            throw Exception("Failed to update user: ${response.message()}")
        }
    }

    suspend fun sendVerificationEmail() {
        val response = apiService.sendVerificationEmail()
        if (!response.isSuccessful) {
            throw Exception("Failed to send verification email: ${response.message()}")
        }
    }

    suspend fun verifyCode(userId: String, code: String) {
        val request = VerificationRequest(userId, code)
        val response = apiService.verifyCode(request)
        if (!response.isSuccessful) {
            throw Exception("Verification failed: ${response.message()}")
        }
    }

    suspend fun updateUserInfo(user: UserRemote) {
        val response = apiService.updateUser(user.userId, user)
        if (!response.isSuccessful) {
            throw Exception("Failed to update user information: ${response.message()}")
        }
    }

    suspend fun getUserNotifications(userId: String): List<NotificationResponse> {
        val response = apiService.getUserNotifications(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch notifications: ${response.message()}")
        }
    }
}

