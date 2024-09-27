package com.crunchquest.android.presentation.ui.profile

import com.crunchquest.android.domain.entities.User

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User?) : ProfileState()
    data class Error(val message: String?) : ProfileState()
}
