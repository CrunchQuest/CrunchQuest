package com.crunchquest.android.presentation.ui.login

import com.crunchquest.android.domain.entities.User

sealed class LoginState {
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}
