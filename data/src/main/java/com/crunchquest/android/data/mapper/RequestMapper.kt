package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.RequestRemote
import com.crunchquest.android.data.source.local.RequestLocal
import com.crunchquest.android.domain.entities.Request
import javax.inject.Inject

class RequestMapper @Inject constructor() {

    // Mapping from remote data model to domain entity
    fun fromRemote(requestRemote: RequestRemote): Request {
        return Request(
            requestId = requestRemote.requestId,
            requesterId = requestRemote.requesterId,
            assistantUserId = requestRemote.assistantUserId,
            title = requestRemote.title,
            description = requestRemote.description,
            categoryId = requestRemote.categoryId,
            status = requestRemote.status,
            latitude = requestRemote.latitude,
            longitude = requestRemote.longitude,
            address = requestRemote.address,
            taskSchedule = requestRemote.taskSchedule,
            createdAt = requestRemote.createdAt,
            updatedAt = requestRemote.updatedAt,
            rewards = requestRemote.rewards,
            notes = requestRemote.notes,
            paymentStatus = requestRemote.paymentStatus,
            paymentMethod = requestRemote.paymentMethod,
            requesterConfirmed = requestRemote.requesterConfirmed,
            assistantConfirmed = requestRemote.assistantConfirmed,
            reviewed = requestRemote.reviewed,
            captureResult = requestRemote.captureResult,
            captureResultTimestamp = requestRemote.captureResultTimestamp
        )
    }

    // Mapping from domain entity to remote data model
    fun toRemote(request: Request): RequestRemote {
        return RequestRemote(
            requestId = request.requestId,
            requesterId = request.requesterId,
            title = request.title,
            description = request.description,
            categoryId = request.categoryId,
            status = request.status,
            latitude = request.latitude,
            longitude = request.longitude,
            address = request.address,
            taskSchedule = request.taskSchedule,
            createdAt = request.createdAt,
            updatedAt = request.updatedAt,
            rewards = request.rewards,
            notes = request.notes,
            paymentStatus = request.paymentStatus,
            paymentMethod = request.paymentMethod,
            requesterConfirmed = request.requesterConfirmed,
            assistantConfirmed = request.assistantConfirmed,
            reviewed = request.reviewed,
            captureResult = request.captureResult,
            captureResultTimestamp = request.captureResultTimestamp
        )
    }

    // Mapping from local data model to domain entity
    fun fromLocal(requestLocal: RequestLocal): Request {
        return Request(
            requestId = requestLocal.requestId,
            requesterId = requestLocal.requesterId,
            assistantUserId = requestLocal.assistantUserId,
            title = requestLocal.title,
            description = requestLocal.description,
            categoryId = requestLocal.categoryId,
            status = requestLocal.status,
            latitude = requestLocal.latitude,
            longitude = requestLocal.longitude,
            address = requestLocal.address,
            taskSchedule = requestLocal.taskSchedule,
            createdAt = requestLocal.createdAt,
            updatedAt = requestLocal.updatedAt,
            rewards = requestLocal.rewards,
            notes = requestLocal.notes,
            paymentStatus = requestLocal.paymentStatus,
            paymentMethod = requestLocal.paymentMethod,
            requesterConfirmed = requestLocal.requesterConfirmed,
            assistantConfirmed = requestLocal.assistantConfirmed,
            reviewed = requestLocal.reviewed,
            captureResult = requestLocal.captureResult,
            captureResultTimestamp = requestLocal.captureResultTimestamp
        )
    }

    // Mapping from domain entity to local data model
    fun toLocal(request: Request): RequestLocal {
        return RequestLocal(
            requestId = request.requestId,
            requesterId = request.requesterId,
            assistantUserId = request.assistantUserId,
            title = request.title,
            description = request.description,
            categoryId = request.categoryId,
            status = request.status,
            latitude = request.latitude,
            longitude = request.longitude,
            address = request.address,
            taskSchedule = request.taskSchedule,
            createdAt = request.createdAt,
            updatedAt = request.updatedAt,
            rewards = request.rewards,
            notes = request.notes,
            paymentStatus = request.paymentStatus,
            paymentMethod = request.paymentMethod,
            requesterConfirmed = request.requesterConfirmed,
            assistantConfirmed = request.assistantConfirmed,
            reviewed = request.reviewed,
            captureResult = request.captureResult,
            captureResultTimestamp = request.captureResultTimestamp
        )
    }
}
