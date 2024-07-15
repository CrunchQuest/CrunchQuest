
package com.crunchquest.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crunchquest.android.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    fun loginUser(email: String, password: String) {
        userRepository.loginUser(email, password)
            .observeForever { success ->
                _loginResult.value = success
            }
    }

    fun checkUserPreferences(uid: String): LiveData<Boolean> {
        return userRepository.checkUserPreferences(uid)
    }
}
