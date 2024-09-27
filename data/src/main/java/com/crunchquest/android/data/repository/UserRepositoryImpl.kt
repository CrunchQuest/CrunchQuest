package com.crunchquest.android.data.repository

import android.net.Uri
import com.crunchquest.android.data.mapper.UserMapper
import com.crunchquest.android.data.source.local.UserLocalDataSource
import com.crunchquest.android.data.source.remote.UserRemoteDataSource
import com.crunchquest.android.domain.entities.Notification
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.repository.UserRepository

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val userMapper: UserMapper
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val userRemote = remoteDataSource.login(email, password)
            if (userRemote.emailVerified) {
                val user = userMapper.fromRemote(userRemote)
                localDataSource.saveUser(userMapper.toLocal(user))
                Result.Success(user)
            } else {
                Result.Error(Exception("Email not verified. Please verify your email first."))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun register(user: User, password: String, idImageUri: Uri, selfieImageUri: Uri): Result<String> {
        return try {
            val userId = remoteDataSource.register(userMapper.toRemote(user), password, idImageUri, selfieImageUri)
            Result.Success(userId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUser(userId: String): Result<User> {
        return try {
            val localUser = localDataSource.getUser(userId)
            if (localUser != null) {
                return Result.Success(userMapper.fromLocal(localUser))
            }
            val userRemote = remoteDataSource.getUser(userId)
            val user = userMapper.fromRemote(userRemote)
            localDataSource.saveUser(userMapper.toLocal(user))
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            remoteDataSource.updateUser(userMapper.toRemote(user))
            localDataSource.saveUser(userMapper.toLocal(user))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun sendVerificationEmail(): Result<Unit> {
        return try {
            remoteDataSource.sendVerificationEmail()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun isUserEmailVerified(): Boolean {
        return localDataSource.isEmailVerified(userId = "userId")
    }

    override suspend fun verifyCode(userId: String, code: String): Result<Unit> {
        return try {
            remoteDataSource.verifyCode(userId, code)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUserInfo(user: User): Result<Unit> {
        return try {
            remoteDataSource.updateUserInfo(userMapper.toRemote(user))
            localDataSource.saveUser(userMapper.toLocal(user))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUserNotifications(userId: String): Result<List<Notification>> {
        return try {
            val notificationsRemote = remoteDataSource.getUserNotifications(userId)
            val notifications = notificationsRemote.map { notificationResponse ->
                Notification(
                    notificationId = notificationResponse.notificationId,
                    userId = userId,
                    type = "defaultType",
                    title = notificationResponse.title,
                    message = notificationResponse.message,
                    createdAt = notificationResponse.createdAt,
                    status = "unread"
                )
            }
            Result.Success(notifications)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val userRemote = remoteDataSource.getUserById(userId)
            val user = userMapper.fromRemote(userRemote)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}


