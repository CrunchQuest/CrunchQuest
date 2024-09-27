package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.AssistantRemote
import com.crunchquest.android.data.source.local.AssistantLocal
import com.crunchquest.android.domain.entities.Assistant
import javax.inject.Inject

class AssistantMapper @Inject constructor(){

    fun fromRemote(assistantRemote: AssistantRemote): Assistant {
        return Assistant(
            assistantId = assistantRemote.assistantId,
            requestId = assistantRemote.requestId,
            assistantUserId = assistantRemote.assistantUserId,
            assistanceStatus = assistantRemote.assistanceStatus,
            proposedRewards = assistantRemote.proposedRewards,
            notes = assistantRemote.notes,
            taskAvailability = assistantRemote.taskAvailability,
            assistConfirmed = assistantRemote.assistConfirmed,
            paymentRequested = assistantRemote.paymentRequested,
            startedExecuting = assistantRemote.startedExecuting,
            createdAt = assistantRemote.createdAt,
            updatedAt = assistantRemote.updatedAt
        )
    }

    fun toRemote(assistant: Assistant): AssistantRemote {
        return AssistantRemote(
            assistantId = assistant.assistantId,
            requestId = assistant.requestId,
            assistantUserId = assistant.assistantUserId,
            assistanceStatus = assistant.assistanceStatus,
            proposedRewards = assistant.proposedRewards,
            notes = assistant.notes,
            taskAvailability = assistant.taskAvailability,
            assistConfirmed = assistant.assistConfirmed,
            paymentRequested = assistant.paymentRequested,
            startedExecuting = assistant.startedExecuting,
            createdAt = assistant.createdAt,
            updatedAt = assistant.updatedAt
        )
    }

    fun fromLocal(assistantLocal: AssistantLocal): Assistant {
        return Assistant(
            assistantId = assistantLocal.assistantId,
            requestId = assistantLocal.requestId,
            assistantUserId = assistantLocal.assistantUserId,
            assistanceStatus = assistantLocal.assistanceStatus,
            proposedRewards = assistantLocal.proposedRewards,
            notes = assistantLocal.notes,
            taskAvailability = assistantLocal.taskAvailability,
            assistConfirmed = assistantLocal.assistConfirmed,
            paymentRequested = assistantLocal.paymentRequested,
            startedExecuting = assistantLocal.startedExecuting,
            createdAt = assistantLocal.createdAt,
            updatedAt = assistantLocal.updatedAt
        )
    }

    fun toLocal(assistant: Assistant): AssistantLocal {
        return AssistantLocal(
            assistantId = assistant.assistantId,
            requestId = assistant.requestId,
            assistantUserId = assistant.assistantUserId,
            assistanceStatus = assistant.assistanceStatus,
            proposedRewards = assistant.proposedRewards,
            notes = assistant.notes,
            taskAvailability = assistant.taskAvailability,
            assistConfirmed = assistant.assistConfirmed,
            paymentRequested = assistant.paymentRequested,
            startedExecuting = assistant.startedExecuting,
            createdAt = assistant.createdAt,
            updatedAt = assistant.updatedAt
        )
    }
}
