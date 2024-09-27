package com.crunchquest.android.presentation.ui.request

import com.crunchquest.android.domain.entities.Request

sealed class CreateRequestState {
    object Loading : CreateRequestState()
    data class Success(val request: Request) : CreateRequestState()
    data class Error(val message: String) : CreateRequestState()
}