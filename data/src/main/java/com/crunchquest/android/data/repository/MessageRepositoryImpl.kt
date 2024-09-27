package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.MessageMapper
import com.crunchquest.android.data.source.local.MessageLocalDataSource
import com.crunchquest.android.data.source.remote.MessageRemoteDataSource
import com.crunchquest.android.domain.entities.Message
import com.crunchquest.android.domain.repository.MessageRepository

class MessageRepositoryImpl(
    private val remoteDataSource: MessageRemoteDataSource,
    private val localDataSource: MessageLocalDataSource,
    private val messageMapper: MessageMapper
) : MessageRepository {

    override suspend fun sendMessage(message: com.crunchquest.android.domain.entities.Message): Result<com.crunchquest.android.domain.entities.Message> {
        return try {
            val messageRemote = messageMapper.toRemote(message)
            remoteDataSource.sendMessage(messageRemote)
            localDataSource.saveMessage(messageMapper.toLocal(message)) // Cache locally
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessage(messageId: String): Result<List<com.crunchquest.android.domain.entities.Message>> {
        return try {
            val messageLocal = localDataSource.getMessage(messageId)
            if (messageLocal != null) {
                return Result.success(listOf(messageMapper.fromLocal(messageLocal)))
            }

            val messageRemote = remoteDataSource.getMessage(messageId)
            if (messageRemote != null) {
                val message = messageMapper.fromRemote(messageRemote)
                localDataSource.saveMessage(messageMapper.toLocal(message)) // Cache locally
                return Result.success(listOf(message))
            } else {
                Result.failure(Exception("Message not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessagesByChat(chatId: String): Result<List<com.crunchquest.android.domain.entities.Message>> {
        return try {
            val localMessages = localDataSource.getMessagesByChat(chatId).map { messageMapper.fromLocal(it) }
            if (localMessages.isNotEmpty()) {
                return Result.success(localMessages)
            }

            val remoteMessages = remoteDataSource.getMessagesByChat(chatId)?.map { messageMapper.fromRemote(it) }
            if (remoteMessages != null) {
                remoteMessages.forEach { message ->
                    localDataSource.saveMessage(messageMapper.toLocal(message)) // Cache locally
                }
                return Result.success(remoteMessages)
            }

            Result.failure(Exception("No messages found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMessageStatus(message: com.crunchquest.android.domain.entities.Message): Result<Unit> {
        return try {
            val messageRemote = messageMapper.toRemote(message)
            remoteDataSource.updateMessageStatus(messageRemote)
            localDataSource.updateMessage(messageMapper.toLocal(message))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMessage(messageId: String): Result<Unit> {
        return try {
            val messageLocal = localDataSource.getMessage(messageId)
            if (messageLocal != null) {
                remoteDataSource.deleteMessage(messageId)
                localDataSource.deleteMessage(messageLocal)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Message not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
