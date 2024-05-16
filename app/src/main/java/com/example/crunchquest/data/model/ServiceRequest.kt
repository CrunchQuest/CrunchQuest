package com.example.crunchquest.data.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@kotlinx.parcelize.Parcelize
@IgnoreExtraProperties
data class ServiceRequest(
    var uid: String? = "",
    var title: String? = "",
    var userUid: String? = "",
    var price: Int? = 0,
    var description: String? = "",
    var category: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var date: String? = null,
    var time: String? = null,
    var address: String? = null,
    var dateBooked: String? = null,
    var timeBooked: String? = null,
    var contactNumber: String? = null,
    var modeOfPayment: String? = null,
    var bookedTo: String? = null,
    var bookedBy: String? = null,
    var categoryId: List<Int>? = null,
    var assistConfirmation: String? = null
) : Parcelable {
    override fun toString(): String {
        return "Service Request Title: $title " +
                "\nRequest Description: $description" +
                "\nTarget Price: $price" +
                "\nCategory: $category" +
                "\nLatitude: $latitude" +
                "\nLongitude: $longitude" +
                "\nDate: $date" +
                "\nTime: $time" +
                "\nAddress: $address" +
                "\nDate Booked: $dateBooked" +
                "\nTime Booked: $timeBooked" +
                "\nContact Number: $contactNumber" +
                "\nMode of Payment: $modeOfPayment" +
                "\nAssist Confirmation: $assistConfirmation"

    }
}