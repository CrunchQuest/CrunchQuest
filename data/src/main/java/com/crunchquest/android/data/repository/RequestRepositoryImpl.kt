package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.RequestMapper
import com.crunchquest.android.data.source.local.RequestLocalDataSource
import com.crunchquest.android.data.source.remote.RequestRemoteDataSource
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.repository.RequestRepository
import com.crunchquest.android.domain.utility.Result

class RequestRepositoryImpl(
    private val remoteDataSource: RequestRemoteDataSource,
    private val localDataSource: RequestLocalDataSource,
    private val requestMapper: RequestMapper
) : RequestRepository {

    // Create a request
    override suspend fun createRequest(request: Request): Result<Request> {
        return try {
            val requestRemote = requestMapper.toRemote(request)
            val createdRequestRemote = remoteDataSource.createRequest(requestRemote)
            val createdRequest = requestMapper.fromRemote(createdRequestRemote)
            localDataSource.saveRequest(requestMapper.toLocal(createdRequest))
            Result.Success(createdRequest)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Get requests by status
    override suspend fun getRequestsByStatus(status: String): Result<List<Request>> {
        return try {
            // Fetch requests from the backend
            val remoteRequests = remoteDataSource.getRequestsByStatus(status).map { requestMapper.fromRemote(it) }

            // Cache each fetched request locally
            remoteRequests.forEach { request ->
                localDataSource.saveRequest(requestMapper.toLocal(request))
            }

            Result.Success(remoteRequests)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    // Get a request by requestId
    override suspend fun getRequest(requestId: String): Result<List<Request>> {
        return try {
            val requestLocal = localDataSource.getRequest(requestId)
            if (requestLocal != null) {
                return Result.Success(listOf(requestMapper.fromLocal(requestLocal)))
            }

            val requestRemote = remoteDataSource.getRequest(requestId)
            if (requestRemote != null) {
                val request = requestMapper.fromRemote(requestRemote)
                localDataSource.saveRequest(requestMapper.toLocal(request)) // Cache locally
                Result.Success(listOf(request))
            } else {
                Result.Error(Exception("Request not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getRequestDetails(requestId: String): Result<Request> {
        return try {
            val requestRemote = remoteDataSource.getRequestDetails(requestId)
            val request = requestMapper.fromRemote(requestRemote)
            Result.Success(request)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getFeaturedRequests(): Result<List<Request>> {
        return try {
            val requestRemotes = remoteDataSource.getFeaturedRequests()
            val requests = requestRemotes.map { requestMapper.fromRemote(it) }
            Result.Success(requests)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Update a request
    override suspend fun updateRequest(request: Request): Result<Unit> {
        return try {
            val requestRemote = requestMapper.toRemote(request)
            remoteDataSource.updateRequest(requestRemote)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Delete a request
    override suspend fun deleteRequest(requestId: String): Result<Unit> {
        return try {
            val requestLocal = localDataSource.getRequest(requestId)
            if (requestLocal != null) {
                remoteDataSource.deleteRequest(requestId)
                localDataSource.deleteRequest(requestLocal)
                Result.Success(Unit)
            } else {
                return Result.Error(Exception("Request not found in local storage"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun cancelRequest(requestId: String): Result<Unit> {
        return try {
            // Assuming remoteDataSource has a method to handle cancellation
            remoteDataSource.cancelRequest(requestId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
