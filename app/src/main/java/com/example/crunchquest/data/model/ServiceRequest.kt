package com.example.crunchquest.data.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@kotlinx.parcelize.Parcelize
@IgnoreExtraProperties
data class ServiceRequest(
    var uid: String? = "", var title: String? = "",
    var userUid: String? = "", var price: Int? = 0,
    var description: String? = "",
    var category: String? = null
) : Parcelable {
    override fun toString(): String {
        return "Service Request Title: $title " +
                "\nRequest Description: $description" +
                "\nTarget Price: $price" +
                "\nCategory: $category"
    }
}