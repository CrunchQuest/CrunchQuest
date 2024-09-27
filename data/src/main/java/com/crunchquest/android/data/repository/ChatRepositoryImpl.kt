package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.ChatMapper
import com.crunchquest.android.data.source.local.ChatLocalDataSource
import com.crunchquest.android.data.source.remote.ChatRemoteDataSource
import com.crunchquest.android.domain.entities.Chat
import com.crunchquest.android.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val remoteDataSource: ChatRemoteDataSource,
    private val localDataSource: ChatLocalDataSource,
    private val chatMapper: ChatMapper
) : ChatRepository {

    override suspend fun createChat(chat: Chat): Result<Chat> {
        return try {
            val chatRemote = chatMapper.toRemote(chat)
            remoteDataSource.createChat(chatRemote)
            localDataSource.saveChat(chatMapper.toLocal(chat)) // Cache locally
            Result.success(chat)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChat(chatId: String): Result<Chat> {
        return try {
            val chatLocal = localDataSource.getChat(chatId)
            if (chatLocal != null) {
                return Result.success(chatMapper.fromLocal(chatLocal))
            }

            val chatRemote = remoteDataSource.getChat(chatId)
            if (chatRemote != null) {
                val chat = chatMapper.fromRemote(chatRemote)
                localDataSource.saveChat(chatMapper.toLocal(chat)) // Cache locally
                return Result.success(chat)
            } else {
                Result.failure(Exception("Chat not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChatsByRequest(requestId: String): Result<List<Chat>> {
        return try {
            val localChats = localDataSource.getChatsByRequest(requestId).map { chatMapper.fromLocal(it) }
            if (localChats.isNotEmpty()) {
                return Result.success(localChats)
            }

            val remoteChats = remoteDataSource.getChatsByRequest(requestId)?.map { chatMapper.fromRemote(it) }
            if (!remoteChats.isNullOrEmpty()) { // Check for null and emptiness
                val localChatList = remoteChats.map { chatMapper.toLocal(it) }
                localDataSource.saveChats(localChatList) // Cache locally
                return Result.success(remoteChats)
            }

            Result.failure(Exception("No chats found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateChat(chat: Chat): Result<Unit> {
        return try {
            val chatRemote = chatMapper.toRemote(chat)
            remoteDataSource.updateChat(chatRemote)
            localDataSource.updateChat(chatMapper.toLocal(chat))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteChat(chatId: String): Result<Unit> {
        return try {
            val chatLocal = localDataSource.getChat(chatId)
            if (chatLocal != null) {
                remoteDataSource.deleteChat(chatId)
                localDataSource.deleteChat(chatLocal)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Chat not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
