package com.crunchquest.android.domain.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
class UserUiModel(
    var uid: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var emailAddress: String? = "",
    var mobileNumber: String? = "",
    var age: Int? = 18,
    var profileImageUrl: String? = null,
    var bio: String? = "",
    var verifiedClient: String? = "NOT_VERIFIED",
    var verifiedServiceProvider: String? = "NOT_VERIFIED",
    var idImageUrl: String? = "",
) : Parcelable

data class RegistrationResult(
    val success: Boolean = false,
    val error: String? = null
)