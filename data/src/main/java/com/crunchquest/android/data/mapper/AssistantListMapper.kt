package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.AssistantListRemote
import com.crunchquest.android.data.source.local.AssistantListLocal
import com.crunchquest.android.domain.entities.AssistantList
import javax.inject.Inject

class AssistantListMapper @Inject constructor(){

    // Convert remote data model to domain entity
    fun fromRemote(assistantListRemote: AssistantListRemote): AssistantList {
        return AssistantList(
            assistantListId = assistantListRemote.assistantListId,
            requestId = assistantListRemote.requestId,
            arrayAssistantUserId = assistantListRemote.arrayAssistantUserId,
            assistanceStatus = assistantListRemote.assistanceStatus,
            isSelectedByRequester = assistantListRemote.isSelectedByRequester,
            createdAt = assistantListRemote.createdAt,
            updatedAt = assistantListRemote.updatedAt
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(assistantList: AssistantList): AssistantListRemote {
        return AssistantListRemote(
            assistantListId = assistantList.assistantListId,
            requestId = assistantList.requestId,
            arrayAssistantUserId = assistantList.arrayAssistantUserId,
            assistanceStatus = assistantList.assistanceStatus,
            isSelectedByRequester = assistantList.isSelectedByRequester,
            createdAt = assistantList.createdAt,
            updatedAt = assistantList.updatedAt
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(assistantListLocal: AssistantListLocal): AssistantList {
        return AssistantList(
            assistantListId = assistantListLocal.assistantListId,
            requestId = assistantListLocal.requestId,
            arrayAssistantUserId = assistantListLocal.arrayAssistantUserId,
            assistanceStatus = assistantListLocal.assistanceStatus,
            isSelectedByRequester = assistantListLocal.isSelectedByRequester,
            createdAt = assistantListLocal.createdAt,
            updatedAt = assistantListLocal.updatedAt
        )
    }

    // Convert domain entity to local data model
    fun toLocal(assistantList: AssistantList): AssistantListLocal {
        return AssistantListLocal(
            assistantListId = assistantList.assistantListId,
            requestId = assistantList.requestId,
            arrayAssistantUserId = assistantList.arrayAssistantUserId,
            assistanceStatus = assistantList.assistanceStatus,
            isSelectedByRequester = assistantList.isSelectedByRequester,
            createdAt = assistantList.createdAt,
            updatedAt = assistantList.updatedAt
        )
    }
}
