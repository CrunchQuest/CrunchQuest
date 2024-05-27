package com.example.crunchquest.ui.buyer.buyer_activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crunchquest.R
import com.example.crunchquest.data.model.Order
import com.example.crunchquest.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var order: Order
    private lateinit var date: TextView
    private lateinit var time: TextView
    private lateinit var price: TextView
    private lateinit var category: TextView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var dateOrdered: TextView
    private lateinit var contactNum: TextView
    private lateinit var mode: TextView
    private lateinit var address: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        // Get the order ID from the intent
        val orderId = intent.getStringExtra("order_id")

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

        // Fetch the order data
        if (orderId != null) {
            fetchOrderDataAndSetFields(orderId)
        } else {
            Toast.makeText(this, "Order ID is missing", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if no order ID is passed
        }
    }

    private fun fetchOrderDataAndSetFields(orderId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/orders/$orderId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedOrder = snapshot.getValue(Order::class.java)
                if (fetchedOrder != null) {
                    order = fetchedOrder

                    // Display the order data
                    date.text = "Date: ${order.date}"
                    time.text = "Time: ${order.time}"
                    price.text = "Price: Rp${order.price}"
                    category.text = "Category: ${order.category}"
                    title.text = "Title: ${order.title}"
                    description.text = "Service Description: ${order.description}"
                    dateOrdered.text = "Date and Time Ordered: ${convertLongToDate(order.dateOrdered)}"
                    mode.text = "Mode of Payment: ${order.modeOfPayment}"
                    address.text = "Address: ${order.address}"

                    // Fetch the service provider's number
                    order.service_provider_uid?.let { fetchServiceProviderNumber(it) }
                } else {
                    Toast.makeText(this@OrderDetailsActivity, "Order not found", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity if the order is not found
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OrderDetailsActivity, "Failed to fetch order data", Toast.LENGTH_SHORT).show()
                finish() // Close the activity on error
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
}