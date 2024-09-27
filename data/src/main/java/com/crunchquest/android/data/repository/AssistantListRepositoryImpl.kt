package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.AssistantListMapper
import com.crunchquest.android.data.source.local.AssistantListLocalDataSource
import com.crunchquest.android.data.source.remote.AssistantListRemoteDataSource
import com.crunchquest.android.domain.entities.AssistantList
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.repository.AssistantListRepository

class AssistantListRepositoryImpl(
    private val remoteDataSource: AssistantListRemoteDataSource,
    private val localDataSource: AssistantListLocalDataSource,
    private val assistantListMapper: AssistantListMapper
) : AssistantListRepository {

    override suspend fun createAssistantList(assistantList: AssistantList): Result<AssistantList> {
        return try {
            val assistantListRemote = assistantListMapper.toRemote(assistantList)
            remoteDataSource.createAssistantList(assistantListRemote)
            localDataSource.saveAssistantList(assistantListMapper.toLocal(assistantList)) // Cache locally
            Result.Success(assistantList)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAssistantList(assistantListId: String): Result<AssistantList> {
        return try {
            val localAssistantList = localDataSource.getAssistantList(assistantListId)?.let { assistantListMapper.fromLocal(it) }
            if (localAssistantList != null) {
                return Result.Success(localAssistantList)
            }

            Result.Error(Exception("Assistant list not found"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAssistantListsByRequest(requestId: String): Result<List<com.crunchquest.android.domain.entities.AssistantList>> {
        return try {
            val localAssistantLists = localDataSource.getAssistantListsByRequest(requestId).map { assistantListMapper.fromLocal(it) }
            if (localAssistantLists.isNotEmpty()) {
                return Result.Success(localAssistantLists)
            }

            Result.Error(Exception("No assistant lists found"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAssistantListByRequest(requestId: String): Result<com.crunchquest.android.domain.entities.AssistantList> {
        return try {
            val localAssistantList = localDataSource.getAssistantListsByRequest(requestId).firstOrNull()?.let { assistantListMapper.fromLocal(it) }
            if (localAssistantList != null) {
                return Result.Success(localAssistantList)
            }

            Result.Error(Exception("No assistant list found"))
        }
        catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateAssistantList(assistantList: com.crunchquest.android.domain.entities.AssistantList): Result<Unit> {
        return try {
            val assistantListRemote = assistantListMapper.toRemote(assistantList)
            remoteDataSource.updateAssistantList(assistantListRemote)
            localDataSource.updateAssistantList(assistantListMapper.toLocal(assistantList))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteAssistantList(assistantListId: String): Result<Unit> {
        return try {
            val assistantListLocal = localDataSource.getAssistantList(assistantListId)
            if (assistantListLocal != null) {
                remoteDataSource.deleteAssistantList(assistantListId)
                localDataSource.deleteAssistantList(assistantListLocal)
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Assistant list not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
