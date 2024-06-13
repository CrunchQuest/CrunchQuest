package com.crunchquest.android.data.model

import android.os.Parcelable
import com.crunchquest.android.ui.general.SendRequirementActivity.Companion.DEFAULT_IMG_URL
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
class User(
    var uid: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var emailAddress: String? = "",
    var mobileNumber: String? = "",
    var age: Int? = 18,
    var profileImageUrl: String? = DEFAULT_IMG_URL,
    var bio: String? = "",
    var verifiedClient: String? = "NOT_VERIFIED",
    var verifiedServiceProvider: String? = "NOT_VERIFIED",
    var idImageUrl: String? = "",
) : Parcelable

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: Throwable) : Result<Nothing>()
}

data class RegistrationResult(
    val success: Boolean = false,
    val error: String? = null
)