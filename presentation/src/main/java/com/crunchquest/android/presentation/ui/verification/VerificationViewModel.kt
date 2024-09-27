package com.crunchquest.android.presentation.ui.verification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.domain.usecase.user.VerificationUseCase
import com.crunchquest.android.domain.utility.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val verificationUseCase: VerificationUseCase
) : ViewModel() {

    private val _verificationState = MutableLiveData<VerificationState>()
    val verificationState: LiveData<VerificationState> get() = _verificationState

    fun verifyCode(userId: String, code: String) {
        // Log the userId and code before making the API call
        Log.d("VerificationViewModel", "verifyCode called with userId: $userId, code: $code")

        _verificationState.value = VerificationState.Loading

        viewModelScope.launch {
            try {
                val result = verificationUseCase(userId, code)
                when (result) {
                    is Result.Success -> {
                        Log.d("VerificationViewModel", "Verification successful for userId: $userId") // Log for success
                        _verificationState.value = VerificationState.Success("Verification successful")
                    }
                    is Result.Error -> {
                        Log.e(
                            "VerificationViewModel",
                            "Verification failed for userId: $userId with error: ${result.exception.message}"
                        ) // Log for failure
                        _verificationState.value = VerificationState.Error(result.exception.message ?: "Verification failed")
                    }
                    Result.Loading -> {
                        // Handle loading state if necessary
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "VerificationViewModel",
                    "Error during verification for userId: $userId with exception: ${e.message}"
                ) // Log the exception
                _verificationState.value = VerificationState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
