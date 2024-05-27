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

        // Initialize the TextViews and Buttons
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
        cancelButton = findViewById(R.id.cancelButton_orderDetails)

        // Get the order data from the intent
        order = intent.getParcelableExtra<Order>("order") ?: throw IllegalArgumentException("Order not passed to OrderDetailsActivity")
        Log.d("OrderDetailsActivity", "Order received: $order")

        // Populate the UI with the order data
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

        if (order.userUid == currentUserId) {
            messageButton.isGone = true
        } else {
            messageButton.setOnClickListener {
                fetchUserAndGoToChatLogActivity()
            }
        }

        // Check order status and set button visibility
        checkStatus()
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
                    val intent = Intent(this@OrderDetailsActivity, ChatLogActivity::class.java)
                    intent.putExtra("user", serviceProvider)
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Account is no longer available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelOrder() {
        Log.d("OrderDetailsActivity", "cancelOrder() called")
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

                        Toast.makeText(this, "Request order cancelled.", Toast.LENGTH_SHORT).show()
                    }
                }

                // Update order status in Firebase
                val ref = FirebaseDatabase.getInstance().getReference("booked_services/$orderUid2")
                ref.child("status").setValue("CANCELLED")
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Cancel Order")
        alert.show()
    }

    private fun confirmTheOrder() {
        Log.d("OrderDetailsActivity", "confirmOrder() called")
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to confirm this order?")
            .setCancelable(true)
            .setPositiveButton("Confirm") { _, _ ->
                val orderUid = order.uid
                val ref = FirebaseDatabase.getInstance().getReference("booked_services/$orderUid")
                ref.child("status").setValue("COMPLETED")
                anotherButton.text = ADD_REVIEW_TEXT
                Toast.makeText(this, "Order confirmed", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirm Order")
        alert.show()
    }
}
