package com.crunchquest.android.presentation.utility

import android.content.Context
import android.content.SharedPreferences
import com.crunchquest.android.domain.entities.User

object UserSessionManager {

    private const val PREF_NAME = "user_session"
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize UserSessionManager, typically called in Application class
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save or update the current user information in SharedPreferences
    fun saveOrUpdateUser(user: User?) {
        user ?: return  // If user is null, do nothing

        val editor = sharedPreferences.edit()
        editor.putString("user_id", user.userId)
        editor.putString("email", user.email)
        editor.putString("first_name", user.firstName)
        editor.putString("last_name", user.lastName)
        editor.putString("bio", user.bio)
        editor.putFloat("avg_rating", user.avgRating?.toFloat() ?: -1f)
        editor.putString("date_of_birth", user.dateOfBirth)
        editor.putString("gender", user.gender)
        editor.putString("phone_number", user.phoneNumber)
        editor.putString("profile_picture", user.profilePicture)
        editor.putString("id_card", user.idCard)
        editor.putString("preference", user.preference)
        editor.putString("created_at", user.createdAt)
        editor.putString("updated_at", user.updatedAt)
        editor.putString("last_login", user.lastLogin)
        editor.putString("status", user.status)
        editor.putString("role", user.role)
        editor.putString("verification_status", user.verificationStatus)
        editor.putString("work_experience", user.workExperience)
        editor.apply()
    }

    // Get the current user information from SharedPreferences
    fun getCurrentUser(): User? {
        if (!this::sharedPreferences.isInitialized) return null

        val userId = sharedPreferences.getString("user_id", null) ?: return null
        val email = sharedPreferences.getString("email", "") ?: ""
        val firstName = sharedPreferences.getString("first_name", "") ?: ""
        val lastName = sharedPreferences.getString("last_name", "") ?: ""
        val bio = sharedPreferences.getString("bio", null)
        val avgRating = sharedPreferences.getFloat("avg_rating", -1f).takeIf { it != -1f }?.toDouble()
        val dateOfBirth = sharedPreferences.getString("date_of_birth", null)
        val gender = sharedPreferences.getString("gender", null)
        val phoneNumber = sharedPreferences.getString("phone_number", "") ?: ""
        val profilePicture = sharedPreferences.getString("profile_picture", null)
        val idCard = sharedPreferences.getString("id_card", null)
        val preference = sharedPreferences.getString("preference", null)
        val createdAt = sharedPreferences.getString("created_at", "") ?: ""
        val updatedAt = sharedPreferences.getString("updated_at", "") ?: ""
        val lastLogin = sharedPreferences.getString("last_login", null)
        val status = sharedPreferences.getString("status", "") ?: ""
        val role = sharedPreferences.getString("role", "") ?: ""
        val verificationStatus = sharedPreferences.getString("verification_status", null)
        val workExperience = sharedPreferences.getString("work_experience", null)

        return User(
            userId = userId,
            email = email,
            firstName = firstName,
            lastName = lastName,
            bio = bio,
            avgRating = avgRating,
            dateOfBirth = dateOfBirth,
            gender = gender,
            phoneNumber = phoneNumber,
            profilePicture = profilePicture,
            idCard = idCard,
            preference = preference,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastLogin = lastLogin,
            status = status,
            role = role,
            verificationStatus = verificationStatus,
            workExperience = workExperience
        )
    }

    // Function to get the current user ID from SharedPreferences
    fun getCurrentUserId(): String? {
        if (!this::sharedPreferences.isInitialized) return null
        return sharedPreferences.getString("user_id", null)
    }

    // Clear the current user information from SharedPreferences
    fun clearCurrentUser() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}






