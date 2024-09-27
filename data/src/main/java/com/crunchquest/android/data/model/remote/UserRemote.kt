package com.crunchquest.android.data.model.remote

import com.google.gson.annotations.SerializedName

data class UserRemote(
    @SerializedName("userId") val userId: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("password") val password: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("avgRating") val avgRating: Double? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String = "",
    @SerializedName("profilePicture") var profilePicture: String? = null,
    @SerializedName("idCard") var idCard: String? = null,
    @SerializedName("preference") val preference: String? = null,
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = "",
    @SerializedName("lastLogin") val lastLogin: String? = null,
    @SerializedName("status") val status: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("verificationStatus") val verificationStatus: String? = null,
    @SerializedName("workExperience") val workExperience: String? = null,
    @SerializedName("emailVerified") val emailVerified: Boolean = false  // Ensure it matches backend field
)


