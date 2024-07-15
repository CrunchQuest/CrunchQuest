
package com.crunchquest.android.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crunchquest.android.data.model.RegistrationResult
import com.crunchquest.android.data.model.User
import com.crunchquest.android.repository.UserRepository

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    private val _emailVerificationResult = MutableLiveData<Boolean>()
    val emailVerificationResult: LiveData<Boolean> = _emailVerificationResult

    fun registerUser(email: String, password: String) {
        userRepository.registerUser(email, password) { task ->
            if (task.isSuccessful) {
                userRepository.sendEmailVerification { emailTask ->
                    _emailVerificationResult.value = emailTask.isSuccessful
                }
            } else {
                _registrationResult.value = RegistrationResult(success = false, error = task.exception?.message)
            }
        }
    }

    fun saveUser(user: User) {
        userRepository.saveUserToFirebaseDatabase(user) { task ->
            _registrationResult.value = RegistrationResult(success = task.isSuccessful, error = task.exception?.message)
        }
    }

    fun initializeUserNotifications(uid: String) {
        userRepository.initializeUserNotifications(uid) { task ->
            // Handle the result if needed
        }
    }

    fun validateInput(fname: String, lname: String, email: String, password: String, confirmPass: String): Boolean {
        return when {
            fname.isEmpty() -> false
            lname.isEmpty() -> false
            email.isEmpty() -> false
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> false
            password.isEmpty() -> false
            password != confirmPass -> false
            else -> true
        }
    }
}
