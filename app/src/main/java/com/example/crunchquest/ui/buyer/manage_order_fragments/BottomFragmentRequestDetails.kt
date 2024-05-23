package com.example.crunchquest.ui.buyer.manage_order_fragments

import android.content.DialogInterface
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

class BottomFragmentRequestDetails(order: Order) : BottomSheetDialogFragment() {
    private var orderClicked = order
    private lateinit var v: View
    private lateinit var date: TextView
    private lateinit var time: TextView
    private lateinit var address: TextView
    private lateinit var price: TextView
    private lateinit var name: TextView
    private lateinit var contactNumber: TextView
    private lateinit var category: TextView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var dandtbooked: TextView
    private lateinit var messageButton: Button
    private lateinit var markButton: Button
    private lateinit var completeButton: Button
    private lateinit var modeEditText: TextView
    private lateinit var reviewButton: Button


    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    companion object {
        const val CONFIRM_TEXT = "CONFIRM BOOKING"
        const val ADD_REVIEW_TEXT = "ADD A REVIEW"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_bottom_booking_details_seller, container, false)
        modeEditText = v.findViewById(R.id.mode_fragmentBottomBookingDetailsSeller)
        time = v.findViewById(R.id.time_fragmentBottomBookingDetailsSeller)
        address = v.findViewById(R.id.address_fragmentBottomBookingDetailsSeller)
        price = v.findViewById(R.id.price_fragmentBottomBookingDetailsSelle)
        name = v.findViewById(R.id.name_fragmentBottomBookingDetailsSeller)
        contactNumber = v.findViewById(R.id.number_fragmentBottomBookingDetailsSeller)
        category = v.findViewById(R.id.category_fragmentBottomBookingDetailsSeller)
        title = v.findViewById(R.id.title_fragmentBottomBookingDetailsSeller)
        description = v.findViewById(R.id.description_fragmentBottomBookingDetailsSeller)
        dandtbooked = v.findViewById(R.id.dandt_fragmentBottomBookingDetailsSeller)
        messageButton = v.findViewById(R.id.button_fragmentBottomBookingDetailsSeller)
        date = v.findViewById(R.id.date_fragmentBottomBookingDetailsSeller)
        markButton = v.findViewById(R.id.marButton_fragmentBottomBookingDetailsSeller)
        completeButton = v.findViewById(R.id.complete_fragmentBottomBookingDetailsSeller)

        checkStatus()

        time.text = "Time: ${orderClicked.time}"
        address.text = "Address: ${orderClicked.address}"
        price.text = "Price: Rp ${orderClicked.price}"
        category.text = "Category: ${orderClicked.category}"
        title.text = "Title: ${orderClicked.title}"
        description.text = "Description: ${orderClicked.description}"
        dandtbooked.text = "Date and Time Booked: ${convertLongToDate(orderClicked.dateOrdered)}"
        date.text = "Date: ${orderClicked.date}"
        modeEditText.text = "Mode of Payment: ${orderClicked.modeOfPayment}"
        fetchNameAndNumber()

        messageButton.setOnClickListener {
            goToChatLogActivity()
        }

        markButton.setOnClickListener {
            acceptOrDeclineOrder()
        }

        completeButton.setOnClickListener {
            if (completeButton.text == CONFIRM_TEXT) {
                markAsComplete()
            } else if (completeButton.text == ADD_REVIEW_TEXT) {
                addAReview()
            }

        }

        statusListener()


        return v
    }

    private fun addAReview() {
        if (orderClicked.reviewed == false) {
            //close the bottom fragment here
            dismissBottomSheet()
            val reviewDialog = ReviewDialog(this, orderClicked,resources.getStringArray(R.array.services_category))
            reviewDialog.showReviewDialog()
        } else if (orderClicked.reviewed == true) {
            Toast.makeText(v.context, "You already submitted a review for this order.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun dismissBottomSheet() {
        val p = requireFragmentManager().findFragmentByTag(FinishedFragment.TAG)!!
        val df = p as BottomSheetDialogFragment
        df.dismiss()
    }

    private fun convertLongToDate(long: Long): String {
        val resultdate = Date(long)
        return resultdate.toString()
    }

    private fun statusListener() {
        val bookedBy = orderClicked.bookedBy
        val bookedTo = orderClicked.bookedTo
        val orderUid = orderClicked.service_booked_uid

        if (bookedBy != null && bookedTo != null && orderUid != null) {
            val ref = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo/$orderUid") // MARKS THISSSSSSSSSSSSS
            val anotherRef = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid/${orderClicked.bookedBy}")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val order = snapshot.getValue(Order::class.java)
                    Log.d("StatusListener", "Order: $order")
                    if (order != null && order.buyerConfirmation == "CONFIRMED" && order.sellerConfirmation == "CONFIRMED") {
                        ref.child("/status").setValue("COMPLETED")
                        anotherRef.child("/status").setValue("COMPLETED")
                        Log.d("StatusListener", "Order status set to COMPLETED for order: $orderUid")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("StatusListener", "onCancelled: ${error.message}")
                }
            })
        } else {
            Log.d("StatusListener", "bookedBy, bookedTo, or orderUid is null. bookedBy: $bookedBy, bookedTo: $bookedTo, orderUid: $orderUid")
        }
    }



    private fun markAsComplete() {
        val bookedBy = orderClicked.bookedBy
        val bookedTo = orderClicked.bookedTo
        val bookingUid = orderClicked.service_booked_uid
        Log.d("MarkAsComplete", "bookedBy: $bookedBy, bookedTo: $bookedTo, bookingUid: $bookingUid")
        if (bookedBy != null && bookedTo != null && bookingUid != null) {
            if (orderClicked.sellerConfirmation == "CONFIRMED") {
                Toast.makeText(v.context, "You already confirmed this order.", Toast.LENGTH_SHORT).show()
                return
            }

            val dialogBuilder = AlertDialog.Builder(v.context)
            dialogBuilder.setMessage("Do you want to confirm that this order is finished?")
                .setCancelable(true)
                .setPositiveButton("Confirm") { _, _ ->
                    if (checkDate(orderClicked.date!!)) {
                        Toast.makeText(v.context, "The Current Date is less than the Booking Date. ", Toast.LENGTH_SHORT).show()
                    } else {
                        val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$bookingUid/${orderClicked.bookedBy}")
                        val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo/$bookingUid")
                        bookedByRef.child("sellerConfirmation").setValue("CONFIRMED")
                        bookedToRef.child("sellerConfirmation").setValue("CONFIRMED")
                        Toast.makeText(v.context, "Booking confirmed as completed.", Toast.LENGTH_SHORT).show()
                        statusListener()
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            val alert = dialogBuilder.create()
            alert.setTitle("Confirmation")
            alert.show()
            dismiss()

            if (orderClicked.sellerConfirmation == "CONFIRMED") {
                Toast.makeText(v.context, "You already confirmed this booking.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("MarkAsComplete", "bookedBy, bookedTo, or bookingUid is null. bookedBy: $bookedBy, bookedTo: $bookedTo, bookingUid: $bookingUid")
        }
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

    private fun acceptOrDeclineOrder() {
        val dialogBuilder = AlertDialog.Builder(v.context)
        dialogBuilder.setMessage("Do you want to Accept this order or Decline?")
            .setCancelable(true)
            .setPositiveButton("Accept", DialogInterface.OnClickListener { _, _ ->
                val bookedBy = orderClicked.bookedBy
                val bookedTo = orderClicked.bookedTo
                val bookingUid = orderClicked.service_booked_uid
                if (bookedBy != null && bookedTo != null && bookingUid != null) {
                    val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$bookingUid/${orderClicked.bookedTo}") // Guide To Set Value Inside booked_by
                    bookedByRef.child("status").setValue("ACCEPTED")
                    val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/${bookedTo}/$bookingUid")
                    bookedToRef.child("status").setValue("ACCEPTED")
                } else {
                    // Handle the case where bookedBy, bookedTo, or bookingUid is null
                    Toast.makeText(context, "Error: Missing order information.", Toast.LENGTH_SHORT).show()
                    Log.d("bookedBy", bookedBy.toString())
                    Log.d("bookedTo", bookedTo.toString())
                    Log.d("bookingUid", bookingUid.toString())
                }
            })
            .setNegativeButton("Decline", DialogInterface.OnClickListener { dialog, _ ->
                val bookingUid = orderClicked.service_booked_uid
                val bookedBy = orderClicked.bookedBy
                val bookedTo = orderClicked.bookedTo
                val orderUid = orderClicked.service_booked_uid
                val orderUid2 = orderClicked.uid
//                if (bookedBy != null && bookedTo != null && bookingUid != null) {
//                    val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$bookingUid")
//                    val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo/$bookingUid")
//                    bookedByRef.removeValue()
//                    bookedToRef.removeValue()
//                    dialog.cancel()
//                } else {
//                    // Handle the case where bookedBy, bookedTo, or bookingUid is null
//                    Toast.makeText(context, "Error: Missing order information.", Toast.LENGTH_SHORT).show()
//                }

                // IF Request Decline Assist
                val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
                if (currentUserUid != orderClicked.bookedTo) {
                    val bookedByRef = orderClicked.bookedBy
                    val bookedToRef = orderClicked.bookedTo
                    val assistConfirmation = orderClicked.assistConfirmation
                    if (bookedToRef != null && assistConfirmation != "FALSE") {
                        if (assistConfirmation == "TRUE") {
                            val ref = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid/${orderClicked.bookedTo}")
                            ref.removeValue().addOnSuccessListener {
                                Log.d("DeclineOrder", "Assist order removed from booked_by/$bookedBy/$orderUid/${orderClicked.bookedTo}")
                            }.addOnFailureListener { e ->
                                Log.e("DeclineOrder", "Failed to remove assist order from booked_by/$bookedBy/$orderUid/${orderClicked.bookedTo}", e)
                            }

                            val anotherRef = FirebaseDatabase.getInstance().getReference("booked_to/${bookedTo}/$orderUid")
                            anotherRef.removeValue().addOnSuccessListener {
                                Log.d("DeclineOrder", "Assist order removed from booked_to/${bookedTo}/$orderUid")
                            }.addOnFailureListener { e ->
                                Log.e("DeclineOrder", "Failed to remove assist order from booked_to/${bookedTo}/$orderUid", e)
                            }

                            Toast.makeText(v.context, "Assist Order Declined.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d("CancelOrder", "assistUser or assistConfirmation is null. bookedBy: $bookedByRef, assistConfirmation: $assistConfirmation")
                    }

                }
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Order")
        alert.show()
    }

    private fun checkStatus() {
        if (orderClicked.status == "NEW") {
            markButton.isGone = false
            completeButton.isGone = true
        } else if (orderClicked.status == "ACCEPTED") {
            markButton.isGone = true
            completeButton.text = CONFIRM_TEXT
            completeButton.isGone = false
        } else {
            markButton.isGone = true
            completeButton.text = ADD_REVIEW_TEXT
            completeButton.isGone = false
        }
    }

    private fun goToChatLogActivity() {
        val ref = FirebaseDatabase.getInstance().getReference("users/${orderClicked.bookedTo}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                val intent = Intent(v.context, ChatLogActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun fetchNameAndNumber() {
        val userUid = orderClicked.bookedTo
        val ref = FirebaseDatabase.getInstance().getReference("users/$userUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("fetchNameAndNumber", "DataSnapshot: $snapshot") // Log the data snapshot
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    name.text = "Name: ${user.firstName} ${user.lastName}"
                    contactNumber.text = "Contact Number: ${user.mobileNumber}"
                } else {
                    name.text = "Name: Account Deleted"
                    contactNumber.text = "Contact Number: Account Deleted"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", error.message)
                Toast.makeText(context, "Failed to fetch user data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}