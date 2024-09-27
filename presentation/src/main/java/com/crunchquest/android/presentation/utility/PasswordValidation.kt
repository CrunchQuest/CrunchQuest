package com.crunchquest.android.presentation.utility

class PasswordValidation {

    data class ValidationResult(
        val hasUppercase: Boolean,
        val hasLowercase: Boolean,
        val hasNumber: Boolean,
        val hasSpecialChar: Boolean,
        val isLengthValid: Boolean
    ) {
        fun isValid(): Boolean {
            return hasUppercase && hasLowercase && hasNumber && hasSpecialChar && isLengthValid
        }
    }

    fun validate(password: String): ValidationResult {
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasNumber = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val isLengthValid = password.length >= 10

        return ValidationResult(
            hasUppercase = hasUppercase,
            hasLowercase = hasLowercase,
            hasNumber = hasNumber,
            hasSpecialChar = hasSpecialChar,
            isLengthValid = isLengthValid
        )
    }
}
