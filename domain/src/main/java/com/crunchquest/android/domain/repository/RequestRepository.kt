package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.utility.Result

interface RequestRepository {
    suspend fun createRequest(request: Request): Result<Request>
    suspend fun getRequestsByStatus(status: String): Result<List<Request>>
    suspend fun getRequest(requestId: String): Result<List<Request>>
    suspend fun getFeaturedRequests(): Result<List<Request>>
    suspend fun getRequestDetails(requestId: String): Result<Request>
    suspend fun updateRequest(request: Request): Result<Unit>
    suspend fun deleteRequest(requestId: String): Result<Unit>
    suspend fun cancelRequest(requestId: String): Result<Unit>
}

