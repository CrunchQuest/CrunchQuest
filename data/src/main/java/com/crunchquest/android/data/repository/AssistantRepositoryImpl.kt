package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.AssistantMapper
import com.crunchquest.android.data.source.local.AssistantLocalDataSource
import com.crunchquest.android.data.source.remote.AssistantRemoteDataSource
import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.repository.AssistantRepository

class AssistantRepositoryImpl(
    private val remoteDataSource: AssistantRemoteDataSource,
    private val localDataSource: AssistantLocalDataSource,
    private val assistantMapper: AssistantMapper
) : AssistantRepository {

    // Propose assistance
    override suspend fun proposeAssistance(assistant: Assistant): Result<Assistant> {
        return try {
            // Convert the domain entity to the remote data model
            val assistantRemote = assistantMapper.toRemote(assistant)

            // Send the assistance proposal to the backend
            val proposedRemoteAssistant = remoteDataSource.proposeAssistance(assistantRemote)

            // Convert the response from the backend to the domain entity
            val proposedAssistant = assistantMapper.fromRemote(proposedRemoteAssistant)

            // Save the assistant locally for offline access
            localDataSource.saveAssistant(assistantMapper.toLocal(proposedAssistant))

            Result.Success(proposedAssistant)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAssistantsByStatus(status: String): Result<List<Assistant>> {
        return try {
            val remoteAssistants = remoteDataSource.getAssistantsByStatus(status)
            val assistants = remoteAssistants.map { assistantMapper.fromRemote(it) }
            assistants.forEach { assistant ->
                localDataSource.saveAssistant(assistantMapper.toLocal(assistant)) // Cache locally
            }
            Result.Success(assistants)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Get assistants by request ID
    override suspend fun getAssistantsByRequest(requestId: String): Result<List<Assistant>> {
        return try {
            // Attempt to fetch from local cache first
            val localAssistants = localDataSource.getAssistantsByRequest(requestId).map { assistantMapper.fromLocal(it) }
            if (localAssistants.isNotEmpty()) {
                return Result.Success(localAssistants)
            }

            // Fetch from remote if local data is not found
            val remoteAssistants = remoteDataSource.getAssistantsByRequest(requestId)?.map { assistantMapper.fromRemote(it) }
            if (remoteAssistants != null && remoteAssistants.isNotEmpty()) {
                remoteAssistants.forEach { assistant ->
                    localDataSource.saveAssistant(assistantMapper.toLocal(assistant)) // Cache each item locally
                }
                Result.Success(remoteAssistants)
            } else {
                Result.Error(Exception("No assistants found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Update an assistant
    override suspend fun updateAssistant(assistant: Assistant): Result<Unit> {
        return try {
            val assistantRemote = assistantMapper.toRemote(assistant)
            remoteDataSource.updateAssistant(assistantRemote)
            localDataSource.updateAssistant(assistantMapper.toLocal(assistant)) // Update locally
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Delete an assistant
    override suspend fun deleteAssistant(assistantId: String): Result<Unit> {
        return try {
            val assistantLocal = localDataSource.getAssistantsByRequest(assistantId).firstOrNull()
            if (assistantLocal != null) {
                remoteDataSource.deleteAssistant(assistantId)
                localDataSource.deleteAssistant(assistantLocal) // Delete locally
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Assistant not found in local storage"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}


