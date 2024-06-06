package com.crunchquest.android.data.model

import android.os.Parcelable
import com.crunchquest.android.data.network.response.ServiceRequestResponse
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
    var paymentUrl: String? = null,
    var bookedTo: String? = null,
    var bookedBy: String? = null,
    var categoryId: List<Int>? = null,
    var assistConfirmation: String? = "FALSE",
    var assistUser: String? = null, // Already included
    var buyerConfirmation: String? = "",
    var buyerReview: String? = "",
    var dateOrdered: Long? = null,
    var name: String? = null,
    var reviewed: Boolean? = false,
    var sellerConfirmation: String? = "",
    var service_booked_uid: String? = null,
    var status: String? = "NEW",
    var mobileNumber: String? = ""
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
                "\nPayment URL: $paymentUrl" +
                "\nAssist Confirmation: $assistConfirmation"

    }
}
fun convertToServiceRequest(serviceRequestResponse: ServiceRequestResponse): ServiceRequest {
    return ServiceRequest(
        uid = serviceRequestResponse.uid,
        address = serviceRequestResponse.request_data.address,
        assistConfirmation = serviceRequestResponse.request_data.assistConfirmation,
        bookedBy = serviceRequestResponse.request_data.bookedBy,
        category = serviceRequestResponse.request_data.category,
        categoryId = serviceRequestResponse.request_data.categoryId,
        date = serviceRequestResponse.request_data.date,
        description = serviceRequestResponse.request_data.description,
        latitude = serviceRequestResponse.request_data.latitude,
        longitude = serviceRequestResponse.request_data.longitude,
        modeOfPayment = serviceRequestResponse.request_data.modeOfPayment,
        price = serviceRequestResponse.request_data.price,
        time = serviceRequestResponse.request_data.time,
        title = serviceRequestResponse.request_data.title,
        userUid = serviceRequestResponse.request_data.userUid
    )
}
