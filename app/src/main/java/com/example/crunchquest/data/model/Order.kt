package com.example.crunchquest.data.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
class Order(var uid: String? = null,
            var service_booked_uid: String? = null,
            var address: String? = null,
            var date: String? = null,
            var name: String? = null,
            var status: String? = "NEW",
            var time: String? = null,
            var price: Int? = 0,
            var dateOrdered: Long = System.currentTimeMillis(),
            var title: String? = null,
            var category: String? = null,
            var description: String? = null,
            var service_provider_uid: String? = null,
            var userUid: String? = null,
            var buyerConfirmation: String = "",
            var sellerConfirmation: String = "",
            var reviewed: Boolean = false,
            var modeOfPayment: String? = "",
            var bookedBy: String? = "",
            var bookedTo: String? = "",

) : Parcelable {
    override fun toString(): String {
        return "Order Title: $title " +
                "\nOrder Description: $description" +
                "\nTarget Price: $price" +
                "\nCategory: $category" +
                "\nDate: $date" +
                "\nTime: $time" +
                "\nAddress: $address" +
                "\nDate Ordered: $dateOrdered" +
                "\nBuyer Confirmation: $buyerConfirmation" +
                "\nSeller Confirmation: $sellerConfirmation" +
                "\nReviewed: $reviewed" +
                "\nMode of Payment: $modeOfPayment" +
                "\nBooked To: $bookedTo"
    }
}