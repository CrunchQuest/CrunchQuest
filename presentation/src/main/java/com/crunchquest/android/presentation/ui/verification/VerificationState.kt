package com.crunchquest.android.presentation.ui.verification

sealed class VerificationState {
    object Loading : VerificationState()
    data class Success(val message: String) : VerificationState()
    data class Error(val message: String) : VerificationState()
}