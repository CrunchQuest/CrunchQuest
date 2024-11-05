package com.crunchquest.android.presentation.ui.register

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.usecase.user.RegisterUserUseCase
import com.crunchquest.android.presentation.utility.PasswordValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import com.crunchquest.android.domain.utility.Result
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val idImageUri = MutableLiveData<Uri>()
    val selfieImageUri = MutableLiveData<Uri>()
    val termsAccepted = MutableLiveData<Boolean>()

    // Validation state LiveData
    private val _isStepOneValid = MutableLiveData<Boolean>()
    val isStepOneValid: LiveData<Boolean> get() = _isStepOneValid

    private val _isStepTwoValid = MutableLiveData<Boolean>()
    val isStepTwoValid: LiveData<Boolean> get() = _isStepTwoValid

    private val _isStepThreeValid = MutableLiveData<Boolean>()
    val isStepThreeValid: LiveData<Boolean> get() = _isStepThreeValid

    private val _registrationResult = MutableLiveData<Result<String>>() // Adjusted to return userId
    val registrationResult: LiveData<Result<String>> get() = _registrationResult

    private var userId: String? = null // Store the userId after registration

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerUser(context: Context) {
        if (!validateInputs()) {
            _registrationResult.value = Result.Error(Exception("Please fill all the required fields and upload images"))
            return
        }

        val user = User(
            userId = UUID.randomUUID().toString(),
            email = email.value ?: "",
            firstName = firstName.value ?: "",
            lastName = lastName.value ?: "",
            bio = null,
            avgRating = null,
            dateOfBirth = null,
            gender = null,
            phoneNumber = "",
            profilePicture = null,
            idCard = null,
            preference = null,
            createdAt = LocalDateTime.now().toString(),
            updatedAt = LocalDateTime.now().toString(),
            lastLogin = null,
            status = "Active",
            role = "User",
            verificationStatus = "Pending",
            workExperience = null
        )

        val idImageUri = idImageUri.value!!
        val selfieImageUri = selfieImageUri.value!!
        val password = password.value!!

        viewModelScope.launch {
            try {
                // Convert Uri to File using the method and the provided context
                val idImageFile = convertUriToFile(idImageUri, context)
                val selfieImageFile = convertUriToFile(selfieImageUri, context)

                // Ensure you handle the case when file conversion fails
                if (idImageFile == null || selfieImageFile == null) {
                    _registrationResult.value = Result.Error(Exception("Failed to access image files"))
                    return@launch
                }

                // Call your use case with the converted files
                val result = registerUserUseCase(user, password, idImageFile, selfieImageFile)
                when (result) {
                    is Result.Success -> {
                        userId = result.data
                        Log.d("RegisterViewModel", "Registration successful, userId: $userId")
                        _registrationResult.value = Result.Success(result.data)
                    }
                    is Result.Error -> {
                        Log.e("RegisterViewModel", "Registration failed with error: ${result.exception.message}")
                        _registrationResult.value = Result.Error(result.exception)
                    }
                    Result.Loading -> {
                        // Optionally handle loading state if needed
                    }
                }
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Error registering user: ${e.message}")
                _registrationResult.value = Result.Error(e)
            }
        }
    }

    private fun convertUriToFile(uri: Uri, context: Context): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile("image", null, context.cacheDir)

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "Error converting Uri to File: ${e.message}")
            null
        }
    }

    // Call this function whenever fields in Step One change
    fun validateStepOne() {
        _isStepOneValid.value = !firstName.value.isNullOrEmpty() &&
                !lastName.value.isNullOrEmpty() &&
                !email.value.isNullOrEmpty()
    }

    // Call this function whenever fields in Step Two change
    fun validateStepTwo() {
        val passwordValue = password.value ?: ""
        val confirmPasswordValue = confirmPassword.value ?: ""

        val validations = PasswordValidation().validate(passwordValue)
        val isPasswordValid = validations.isValid() && passwordValue == confirmPasswordValue

        _isStepTwoValid.value = isPasswordValid
    }

    // Call this function whenever fields in Step Three change
    fun validateStepThree() {
        _isStepThreeValid.value = idImageUri.value != null && selfieImageUri.value != null
    }

    private fun validateInputs(): Boolean {
        // Implement actual validation logic
        val isValid = !firstName.value.isNullOrEmpty() &&
                !lastName.value.isNullOrEmpty() &&
                !email.value.isNullOrEmpty() &&
                !password.value.isNullOrEmpty() &&
                password.value == confirmPassword.value &&
                idImageUri.value != null &&
                selfieImageUri.value != null

        return isValid
    }
}




