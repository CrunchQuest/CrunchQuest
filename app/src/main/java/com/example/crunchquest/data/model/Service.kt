package com.example.crunchquest.data.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
class Service(
    var uid: String? = "", var title: String? = "", var description: String? = "",
    var price: Int? = 0, var category: String? = "", var userUid: String? = "",
    var status: String? = "ACTIVE"
) : Parcelable {
    override fun toString(): String {
        return "${title!!.toUpperCase()}"
    }
}