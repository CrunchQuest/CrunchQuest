package com.example.crunchquest.data.model

import android.os.Parcelable
import com.example.crunchquest.ui.general.SignUpActivity
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
    var profileImageUrl: String? = "${SignUpActivity.DEFAULT_IMG_URL}",
    var bio: String? = "",
    var verifiedClient: String? = "NOT_VERIFIED",
    var verifiedServiceProvider: String? = "NOT_VERIFIED"
) : Parcelable