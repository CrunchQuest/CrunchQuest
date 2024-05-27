package com.example.crunchquest.ui.buyer.buyer_activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.crunchquest.R
import com.example.crunchquest.data.model.Order
import com.example.crunchquest.data.model.User
import com.example.crunchquest.ui.buyer.manage_order_fragments.BottomFragmentOrderDetails
import com.example.crunchquest.ui.messages.ChatLogActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var date: TextView
    private lateinit var time: TextView
    private lateinit var price: TextView
    private lateinit var category: TextView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var dateOrdered: TextView
    private lateinit var contactNum: TextView
    private lateinit var address: TextView
    private lateinit var mode: TextView
    private lateinit var googleMap: GoogleMap
    private lateinit var anotherButton: Button
    private lateinit var cancelButton: Button
    private lateinit var messageButton: Button
    private lateinit var order: Order

    companion object {
        const val CONFIRM_TEXT = "CONFIRM BOOKING"
        const val ADD_REVIEW_TEXT = "ADD A REVIEW"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        // Initialize the TextViews
        mode = findViewById(R.id.mode_orderDetails)
        date = findViewById(R.id.date_orderDetails)
        time = findViewById(R.id.time_orderDetails)
        price = findViewById(R.id.price_orderDetails)
        category = findViewById(R.id.category_orderDetails)
        title = findViewById(R.id.title_orderDetails)
        description = findViewById(R.id.description_orderDetails)
        dateOrdered = findViewById(R.id.dateAndTimeOrdered_orderDetails)
        contactNum = findViewById(R.id.number_orderDetails)
        address = findViewById(R.id.address_fragmentBottomBookingDetails)
        messageButton = findViewById(R.id.button_orderDetails)
        anotherButton = findViewById(R.id.anotherButton_orderDetails)
        cancelButton= findViewById(R.id.cancelButton_orderDetails)

        // Get the order data from the intent
        val order = intent.getParcelableExtra<Order>("order") ?: throw IllegalArgumentException("Order not passed to OrderDetailsActivity")

        if (order != null) {
            date.text = order.date
            time.text = order.time
            price.text = order.price.toString()
            category.text = order.category
            title.text = order.title
            description.text = order.description
            dateOrdered.text = convertLongToDate(order.dateOrdered)
            address.text = order.address
            mode.text = "Mode of Payment: ${order.modeOfPayment}"

            // Fetch contact number
            order.service_provider_uid?.let { fetchContactNumber(it) }
        }

        // Initialize the map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        anotherButton.setOnClickListener {
            if (anotherButton.text == CONFIRM_TEXT) {
                confirmTheOrder()
            } else {
                Toast.makeText(this, "You already confirmed this booking.", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            cancelOrder()
        }

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        if (order?.userUid == currentUserId) {
            messageButton.isGone = true
        } else {
            messageButton.setOnClickListener {
                fetchUserAndGoToChatLogActivity()
            }
        }

        return
    }

    private fun fetchContactNumber(serviceProviderUid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("users/$serviceProviderUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val serviceProvider = snapshot.getValue(User::class.java)
                contactNum.text = serviceProvider?.mobileNumber ?: "N/A"
            }

            override fun onCancelled(error: DatabaseError) {
                contactNum.text = "N/A"
            }
        })
    }

    private fun fetchServiceProviderNumber(serviceProviderUid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("users/$serviceProviderUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val serviceProvider = snapshot.getValue(User::class.java)
                if (serviceProvider != null) {
                    contactNum.text = "Contact Number: ${serviceProvider.mobileNumber}"
                } else {
                    contactNum.text = "Contact Number: Account Deleted"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                contactNum.text = "Contact Number: Failed to fetch"
            }
        })
    }

    private fun convertLongToDate(long: Long): String {
        val resultDate = Date(long)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(resultDate)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Configure your map here

        // Fetch the order data and set the map location
        setMapLocation()
    }

    private fun setMapLocation() {
        val latitude = order.latitude ?: 0.0
        val longitude = order.longitude ?: 0.0

        if (latitude != 0.0 || longitude != 0.0) {
            val userLocation = LatLng(latitude, longitude)
            googleMap.addMarker(MarkerOptions().position(userLocation).title("Marker in User Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16.0f))
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
                anotherButton.text = BottomFragmentOrderDetails.CONFIRM_TEXT
                cancelButton.isGone = true
            }
            "COMPLETED" -> {
                anotherButton.isGone = true
                anotherButton.text = BottomFragmentOrderDetails.ADD_REVIEW_TEXT
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
                    val intent = Intent(this@OrderDetailsActivity, ChatLogActivity::class.java)
                    intent.putExtra("user", serviceProvider)
                    startActivity(intent)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Account is no longer Available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelOrder() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to cancel this order?")
            .setCancelable(true)
            .setPositiveButton("Confirm") { _, _ ->
                val bookedBy = order.bookedBy
                val bookedTo = order.bookedTo
                val orderUid = order.service_booked_uid
                val orderUid2 = order.uid

                // IF Assist Cancel Assisting
                val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
                if (currentUserUid == order.bookedTo) {
                    val bookedByRef = order.bookedBy
                    val assistConfirmation = order.assistConfirmation
                    if (bookedByRef != null && assistConfirmation != "FALSE") {
                        if (assistConfirmation == "TRUE") {
                            val ref = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid/$currentUserUid")
                            ref.removeValue().addOnSuccessListener {
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

                            Toast.makeText(this, "Assist order cancelled.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d("CancelOrder", "assistUser or assistConfirmation is null. bookedBy: $bookedByRef, assistConfirmation: $assistConfirmation")
                    }

                    // Request Cancel Requesting
                    if (currentUserUid == order.bookedBy) {
                        val ref = FirebaseDatabase.getInstance().getReference("booked_by")
                        ref.removeValue().addOnSuccessListener {
                            Log.d("Cancel All Order", "Order removed from booked_by/$bookedBy/$orderUid/${order.bookedBy}")
                        }.addOnFailureListener { e ->
                            Log.e("Cancel All Order", "Failed to remove order from booked_by/$bookedBy/$orderUid/${order.bookedBy}", e)
                        }

                        val anotherRef = FirebaseDatabase.getInstance().getReference("booked_to")
                        anotherRef.removeValue().addOnSuccessListener {
                            Log.d("Cancel All Order", "Order removed from booked_to/$bookedTo/$orderUid")
                        }.addOnFailureListener { e ->
                            Log.e("Cancel All Order", "Failed to remove order from booked_to/$bookedTo/$orderUid", e)
                        }

                        val anotherAnotherRef = FirebaseDatabase.getInstance().getReference("service_requests/${order.uid}/$orderUid2")
                        anotherAnotherRef.removeValue().addOnSuccessListener {
                            Log.d("Cancel All Order", "Order removed from service_requests/$orderUid2")
                        }.addOnFailureListener { e ->
                            Log.e("Cancel All Order", "Failed to remove order from service_requests/$orderUid2", e)
                        }

                        Toast.makeText(this, "Order cancelled.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("Cancel All Order", "Log Request Cancel: bookedBy, bookedTo, or orderUid. bookedBy: $bookedBy, bookedTo: $bookedTo, orderUid: $orderUid")
                    }

                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Cancel Order")
        alert.show()
    }

    private fun confirmTheOrder() {
        if (order.buyerConfirmation != "CONFIRMED") {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Do you want to confirm that this order is finished?")
                .setCancelable(true)
                .setPositiveButton("Confirm") { _, _ ->
                    val bookedBy = order.bookedBy
                    val bookedTo = order.bookedTo
                    val orderUid = order.service_booked_uid
                    Log.d("ConfirmOrder", "bookedBy: $bookedBy, bookedTo: $bookedTo, orderUid: $orderUid")
                    if (bookedBy != null && bookedTo != null && orderUid != null) {
                        if (checkDate(order.date!!)) {
                            Toast.makeText(this, "The Current Date is less than the Booking Date. ", Toast.LENGTH_SHORT).show()
                        } else {
                            val ref = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid/${order.bookedTo}")
                            val anotherRef = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo/$orderUid")
                            anotherRef.child("buyerConfirmation").setValue("CONFIRMED")
                            Toast.makeText(this, "Booking confirmed as completed.", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "You already confirmed this booking.", Toast.LENGTH_SHORT).show()
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

                        // Initialize the map fragment
                        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                        mapFragment?.getMapAsync { googleMap ->
                            // Configure your map here
                            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                            // Get latitude and longitude from the fetched Order object
                            val latitude = order.latitude ?: 0.0
                            val longitude = order.longitude ?: 0.0

                            // Check if both latitude and longitude are not 0.0
                            if (latitude != 0.0 || longitude != 0.0) {
                                // Create a LatLng object using the latitude and longitude
                                val userLocation = LatLng(latitude, longitude)

                                // Add a marker at the user's location and move the camera
                                googleMap.addMarker(MarkerOptions().position(userLocation).title("Marker in User Location"))

                                val zoomLevel = 16.0f
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel))
                            }
                        }
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

    private fun statusListenerRequest() {
        val bookedBy = order.bookedBy
        val bookedTo = order.bookedTo
        val orderUid = order.service_booked_uid

        if (bookedBy != null && bookedTo != null && orderUid != null) {
            val ref = FirebaseDatabase.getInstance().getReference("booked_by/$bookedBy/$orderUid/${order.bookedBy}")
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
}
