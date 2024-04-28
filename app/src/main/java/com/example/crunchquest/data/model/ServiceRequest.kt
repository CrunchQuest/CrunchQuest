package com.example.crunchquest.data.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ServiceRequest(
    var uid: String? = "", var title: String? = "",
    var userUid: String? = "", var price: Int? = 0,
    var description: String? = "",
    var category: String? = null
) : Serializable {
    override fun toString(): String {
        return "Service Request Title: $title " +
                "\nRequest Description: $description" +
                "\nTarget Price: $price" +
                "\nCategory: $category"
    }
}