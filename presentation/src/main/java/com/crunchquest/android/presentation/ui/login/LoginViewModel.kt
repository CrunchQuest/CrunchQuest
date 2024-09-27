
package com.crunchquest.android.presentation.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.domain.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.crunchquest.android.domain.utility.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    // LiveData to handle navigation events
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val result = loginUseCase(email, password)
                when (result) {
                    is Result.Success -> {
                        val user = result.data
                        Log.d("LoginViewModel", "Login successful: $user")  // Log for debugging
                        _loginState.value = LoginState.Success(user)
                        // Trigger navigation event
                        _navigateToHome.value = true
                    }
                    is Result.Error -> {
                        Log.e("LoginViewModel", "Login failed: ${result.exception.message}")  // Log for debugging
                        _loginState.value = LoginState.Error(result.exception.message ?: "Unknown error")
                    }
                    Result.Loading -> {
                        // Handle loading state if needed, although it's already set initially
                        _loginState.value = LoginState.Loading
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login error: ${e.message}")  // Log the error
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Reset navigation event after handling
    fun onNavigationHandled() {
        _navigateToHome.value = false
    }
}





