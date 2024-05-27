@file:Suppress("DEPRECATION")

package com.example.crunchquest.ui.buyer.manage_order_fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import com.example.crunchquest.R
import com.example.crunchquest.data.model.Order
import com.example.crunchquest.data.model.User
import com.example.crunchquest.ui.dialogs.ReviewDialog
import com.example.crunchquest.ui.messages.ChatLogActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date


class BottomFragmentOrderDetails(orderPassed: Order) : BottomSheetDialogFragment() {
    private lateinit var v: View
    private var orderClicked = orderPassed
    private lateinit var date: TextView
    private lateinit var time: TextView
    private lateinit var price: TextView
    private lateinit var category: TextView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var dateOrdered: TextView
    private lateinit var contactNum: TextView
    private lateinit var messageButton: Button
    private lateinit var anotherButton: Button
    private lateinit var address: TextView
    private lateinit var mode: TextView
    private lateinit var cancelButton: Button
    val order = orderPassed

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    companion object {
        const val CONFIRM_TEXT = "CONFIRM BOOKING"
        const val ADD_REVIEW_TEXT = "ADD A REVIEW"

    }

//    override fun onStart() {
//        super.onStart()
//
//        val bottomSheetDialog = dialog as BottomSheetDialog?
//        val bottomSheetInternal = bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//        bottomSheetInternal?.let {
//            val layoutParams = it.layoutParams
//            if (layoutParams is CoordinatorLayout.LayoutParams) {
//                val behavior = layoutParams.behavior as BottomSheetBehavior<*>?
//                behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                    override fun onStateChanged(bottomSheet: View, newState: Int) {
//                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                            val intent = Intent(context, OrderDetailsActivity::class.java)
//                            intent.putExtra("order", order)
//                            startActivity(intent)
//                            dismiss()
//                        }
//                    }
//
//                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//                })
//            }
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bottom_booking_details, container, false)
        mode = v.findViewById(R.id.mode_orderDetails)
        date = v.findViewById(R.id.date_orderDetails)
        time = v.findViewById(R.id.time_orderDetails)
        price = v.findViewById(R.id.price_orderDetails)
        category = v.findViewById(R.id.category_orderDetails)
        title = v.findViewById(R.id.title_orderDetails)
        description = v.findViewById(R.id.description_orderDetails)
        dateOrdered = v.findViewById(R.id.dateAndTimeOrdered_orderDetails)
        contactNum = v.findViewById(R.id.number_orderDetails)
        messageButton = v.findViewById(R.id.button_orderDetails)
        anotherButton = v.findViewById(R.id.anotherButton_orderDetails)
        address = v.findViewById(R.id.address_fragmentBottomBookingDetails)
        cancelButton= v.findViewById(R.id.cancelButton_orderDetails)


        date.text = "${order.date}"
        time.text = "${order.time}"
        price.text = "${order.price.toString()}"
        category.text = "${order.category}"
        title.text = "${order.title}"
        description.text = "${order.description}"
        dateOrdered.text = "${convertLongToDate(order.dateOrdered)}"
        address.text = "${order.address}"
        mode.text = "Mode of Payment: ${order.modeOfPayment}"


        fetchNumber()
        checkStatus()
        statusListenerRequest()
        statusListenerAssist()
        anotherButton.setOnClickListener {
            if (anotherButton.text == CONFIRM_TEXT) {
                confirmTheOrder()
            } else {
                Toast.makeText(v.context, "You already confirmed this booking.", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            cancelOrder()
        }

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        if (order.userUid == currentUserId) {
            messageButton.isGone = true
        } else {
            messageButton.setOnClickListener {
                fetchUserAndGoToChatLogActivity()
            }
        }

        return v
    }

    private fun cancelOrder() {
        val dialogBuilder = AlertDialog.Builder(v.context)
        dialogBuilder.setMessage("Do you want to cancel this order?")
            .setCancelable(true)
            .setPositiveButton("Confirm") { _, _ ->
                val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
                val bookedBy = order.bookedBy
                val bookedTo = order.bookedTo
                var orderUid = order.service_booked_uid
                val orderUid2 = order.uid

                // Log initial order details
                Log.d("CancelOrder", "Current User UID: $currentUserUid")
                Log.d("CancelOrder", "Order Details: bookedBy: $bookedBy, bookedTo: $bookedTo, orderUid: $orderUid, orderUid2: $orderUid2")

                // Check if orderUid is null and handle accordingly
                if (orderUid2 == null) {
                    // Try to derive orderUid from other fields or handle the error
                    Log.e("CancelOrder", "orderUid is null. Cannot proceed with cancellation.")
                    Toast.makeText(v.context, "Unable to cancel order: orderUid is missing.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Handle Assist Cancel Assisting
                if (currentUserUid == bookedTo) {
                    val assistConfirmation = order.assistConfirmation
                    if (assistConfirmation == "TRUE") {
                        val assistRef = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid/$currentUserUid")
                        assistRef.removeValue().addOnSuccessListener {
                            Log.d("CancelOrder", "Assist order removed from booked_by/$bookedBy/$orderUid/$currentUserUid")
                        }.addOnFailureListener { e ->
                            Log.e("CancelOrder", "Failed to remove assist order from booked_by/$bookedBy/$orderUid/$currentUserUid", e)
                        }

                        val anotherRef = FirebaseDatabase.getInstance().getReference("booked_to/$currentUserUid/$orderUid")
                        anotherRef.removeValue().addOnSuccessListener {
                            Log.d("CancelOrder", "Assist order removed from booked_to/$currentUserUid/$orderUid/$currentUserUid")
                        }.addOnFailureListener { e ->
                            Log.e("CancelOrder", "Failed to remove assist order from booked_to/$currentUserUid/$orderUid/$currentUserUid", e)
                        }

                        Toast.makeText(v.context, "Assist order cancelled.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("CancelOrder", "assistConfirmation is null or FALSE. assistConfirmation: $assistConfirmation")
                    }
                }

                // Handle Request Cancel Requesting
                if (currentUserUid == bookedBy) {
                    val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid2")
                    bookedByRef.removeValue().addOnSuccessListener {
                        Log.d("CancelRequestOrder", "Order removed from booked_by/$bookedBy/$orderUid2")
                    }.addOnFailureListener { e ->
                        Log.e("CancelRequestOrder", "Failed to remove order from booked_by/$bookedBy/$orderUid2", e)
                    }

                    val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo/$orderUid")
                    bookedToRef.removeValue().addOnSuccessListener {
                        Log.d("CancelRequestOrder", "Order removed from booked_to/$bookedTo/$orderUid")
                    }.addOnFailureListener { e ->
                        Log.e("CancelRequestOrder", "Failed to remove order from booked_to/$bookedTo/$orderUid", e)
                    }

                    val serviceRequestsRef = FirebaseDatabase.getInstance().getReference("service_requests/$orderUid2")
                    serviceRequestsRef.removeValue().addOnSuccessListener {
                        Log.d("CancelRequestOrder", "Order removed from service_requests/$currentUserUid/$orderUid2")
                    }.addOnFailureListener { e ->
                        Log.e("CancelRequestOrder", "Failed to remove order from service_requests/$currentUserUid/$orderUid2", e)
                    }

                    Toast.makeText(v.context, "Order cancelled.", Toast.LENGTH_SHORT).show()
                }

//                // Handle Request Cancel All Order
//                if (currentUserUid == bookedBy && bookedTo != null && orderUid2 != null) {
//                    val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy")
//                    bookedByRef.removeValue().addOnSuccessListener {
//                        Log.d("CancelRequestAll", "Order removed from booked_by/$bookedBy")
//                    }.addOnFailureListener { e ->
//                        Log.e("CancelRequestAll", "Failed to remove order from booked_by/$bookedBy", e)
//                    }
//
//                    val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo")
//                    bookedToRef.removeValue().addOnSuccessListener {
//                        Log.d("CancelRequestAll", "Order removed from booked_to/$bookedTo")
//                    }.addOnFailureListener { e ->
//                        Log.e("CancelRequestAll", "Failed to remove order from booked_to/$bookedTo", e)
//                    }
//
//                    val serviceRequestsRef = FirebaseDatabase.getInstance().getReference("service_requests/$orderUid2")
//                    serviceRequestsRef.removeValue().addOnSuccessListener {
//                        Log.d("CancelRequestAll", "Order removed from service_requests/$currentUserUid/$orderUid2")
//                    }.addOnFailureListener { e ->
//                        Log.e("CancelRequestAll", "Failed to remove order from service_requests/$currentUserUid/$orderUid2", e)
//                    }
//
//                    Toast.makeText(v.context, "Order cancelled.", Toast.LENGTH_SHORT).show()
//                }

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Cancel Order")
        alert.show()
        dismissBottomSheet()
    }




    private fun statusListenerRequest() {
        val bookedBy = order.bookedBy
        val bookedTo = order.bookedTo
        val orderUid = orderClicked.uid

        if (bookedBy != null && bookedTo != null && orderUid != null) {
            val ref = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid/${orderClicked.bookedBy}")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val order = snapshot.getValue(Order::class.java)
                    if (order != null && order.buyerConfirmation == "CONFIRMED" && order.sellerConfirmation == "CONFIRMED") {
                        ref.child("/status").setValue("COMPLETED")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } else {
            Log.d("StatusListenerRequest", "bookedBy, bookedTo, or orderUid is null. bookedBy: $bookedBy, bookedTo: $bookedTo, orderUid: $orderUid")
        }
    }

    private fun statusListenerAssist() {
        val bookedBy = order.bookedBy
        val bookedTo = order.bookedTo
        val orderUid = order.service_booked_uid

        if (bookedBy != null && bookedTo != null && orderUid != null) {
            val ref = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo/$orderUid")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val order = snapshot.getValue(Order::class.java)
                    if (order != null && order.buyerConfirmation == "CONFIRMED" && order.sellerConfirmation == "CONFIRMED") {
                        ref.child("/status").setValue("COMPLETED")

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error here
                }
            })
        } else {
            Log.d("StatusListenerAssist", "bookedBy, bookedTo, or orderUid is null. bookedBy: $bookedBy, bookedTo: $bookedTo, orderUid: $orderUid")
        }
    }



    private fun confirmTheOrder() {
        if (order.buyerConfirmation != "CONFIRMED") {
            val dialogBuilder = AlertDialog.Builder(v.context)
            dialogBuilder.setMessage("Do you want to confirm that this order is finished?")
                .setCancelable(true)
                .setPositiveButton("Confirm") { _, _ ->
                    val bookedBy = order.bookedBy
                    val bookedTo = order.bookedTo
                    val orderUid = order.service_booked_uid
                    Log.d("ConfirmOrder", "bookedBy: $bookedBy, bookedTo: $bookedTo, orderUid: $orderUid")
                    if (bookedBy != null && bookedTo != null && orderUid != null) {
                        if (checkDate(order.date!!)) {
                            Toast.makeText(v.context, "The Current Date is less than the Booking Date. ", Toast.LENGTH_SHORT).show()
                        } else {
                            val ref = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid/${orderClicked.bookedTo}")
                            val anotherRef = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo/$orderUid")
                            anotherRef.child("buyerConfirmation").setValue("CONFIRMED")
                            Toast.makeText(v.context, "Booking confirmed as completed.", Toast.LENGTH_SHORT).show()
                            ref.child("buyerConfirmation").setValue("CONFIRMED")
                            statusListenerRequest()
                            statusListenerAssist()
                        }
                    } else {
                        Log.d("ConfirmOrder", "bookedBy, bookedTo, or orderUid is null. bookedBy: $bookedBy, bookedTo: $bookedTo, orderUid: $orderUid")
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            val alert = dialogBuilder.create()
            alert.setTitle("Confirmation")
            alert.show()
        } else {
            Toast.makeText(v.context, "You already confirmed this booking.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addAReview() {
        if (order.reviewed == false) {
            //close the bottom frafment here
            dismissBottomSheet()
            val reviewDialog = ReviewDialog(this@BottomFragmentOrderDetails, order,resources.getStringArray(R.array.services_category))
            reviewDialog.showReviewDialog()
        } else if (order.reviewed == true) {
            Toast.makeText(v.context, "You already submitted a review for this order.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun checkStatus() {
        when (order.status) {
            "NEW" -> {
                anotherButton.isGone = true
                cancelButton.isGone = false
            }
            "ACCEPTED" -> {
                anotherButton.isGone = false
                anotherButton.text = CONFIRM_TEXT
                cancelButton.isGone = true
            }
            "COMPLETED" -> {
                anotherButton.isGone = true
                anotherButton.text = ADD_REVIEW_TEXT
                cancelButton.isGone = true
            }
        }
    }

    private fun fetchUserAndGoToChatLogActivity() {
        try {
            val ref = FirebaseDatabase.getInstance().getReference("users/${order.bookedBy}")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val serviceProvider = snapshot.getValue(User::class.java)!!
                    val intent = Intent(v.context, ChatLogActivity::class.java)
                    intent.putExtra("user", serviceProvider)
                    startActivity(intent)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        } catch (e: Exception) {
            Toast.makeText(v.context, "Account is no longer Available.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun fetchNumber() {
        try {
            val ref = FirebaseDatabase.getInstance().getReference("users/${order.service_provider_uid}")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val serviceProvider = snapshot.getValue(User::class.java)
                    if (serviceProvider != null) {
                        contactNum.text = "${contactNum.text} ${serviceProvider.mobileNumber}"
                    } else {
                        contactNum.visibility = View.GONE
//                        contactNum.text = "${contactNum.text} Account Deleted"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        } catch (e: Exception) {
            contactNum.text = "${contactNum.text} Account Deleted"
        }
    }

    private fun dismissBottomSheet() {
        val p = requireFragmentManager().findFragmentByTag(FinishedFragment.TAG)!!
        val df = p as BottomSheetDialogFragment
        df.dismiss()
    }


    fun convertLongToDate(long: Long): String {
        val resultdate = Date(long)
        return resultdate.toString()
    }

    private fun checkDate(date: String): Boolean{
        val values = date.split(" ").toTypedArray()
        val month = convertMonth(values[0])
        val day = values[1].substring(0,2)
        val year = values[2]
        val longDate = "$year$month$day".toLong()
        val currentDate = convertLongToTime(System.currentTimeMillis()).toLong()
        Log.d("INeedThisValue", "Month: ${values[0]}")
        Log.d("INeedThisValue", "Day: ${values[1]}")
        Log.d("INeedThisValue", "Year: ${values[2]}")
        Log.d("INeedThisValue", "Date Needed: $longDate")
        Log.d("INeedThisValue", "Current Date: $currentDate")
        return currentDate < longDate
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyyMMdd")
        return format.format(date)
    }

    private fun convertMonth(s: String): String {
        var returnValue = "";
        when(s.toUpperCase()) {
            "JANUARY" -> { returnValue = "01" }
            "FEBRUARY" -> { returnValue = "02" }
            "MARCH" -> { returnValue = "03" }
            "APRIL" -> { returnValue = "04" }
            "MAY" -> { returnValue = "05" }
            "JUNE" -> { returnValue = "06" }
            "JULY" -> { returnValue = "07" }
            "AUGUST" -> { returnValue = "08" }
            "SEPTEMBER" -> { returnValue = "09" }
            "OCTOBER" -> { returnValue = "10" }
            "NOVEMBER" -> { returnValue = "11" }
            "DECEMBER" -> { returnValue = "12" }
        }

        return returnValue
    }


}