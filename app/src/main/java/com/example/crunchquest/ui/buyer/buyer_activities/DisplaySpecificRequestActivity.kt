@file:Suppress("DEPRECATION")

package com.example.crunchquest.ui.buyer.buyer_activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.crunchquest.R
import com.example.crunchquest.data.model.Order
import com.example.crunchquest.data.model.Service
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.data.model.User
import com.example.crunchquest.data.model.UserSellerInfo
import com.example.crunchquest.ui.messages.ChatLogActivity
import com.example.crunchquest.utility.handlers.ServiceRequestHandler
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.Date


class DisplaySpecificRequestActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var toolBar: Toolbar

    private lateinit var userUid: String
    private lateinit var serviceUid: String
    var serviceRequestHandler = ServiceRequestHandler()

    //nmap -> this is for the map
    private lateinit var mMap: GoogleMap

    //user -> this is for the first card view where all informations are from the model User
    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView

    // for the order details
    private lateinit var dateButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var timePicker: TextView
    private lateinit var addressTextView: TextView
    private lateinit var dateAndTimeTextView: TextView
    private lateinit var contactNumber: TextView
    private lateinit var modeOfPayment: TextView

    //for the service details
    private lateinit var serviceTitleTextView: TextView
    private lateinit var serviceDescriptionTextView: TextView
    private lateinit var serviceCategoryTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var assistButton: Button

    //Uer review buttoon
//    private lateinit var userReviewButton: Button

    //floating action button
    private lateinit var floatingActionButton: FloatingActionButton

    //showProfileFragment
    private lateinit var showProfileImageBtn: ImageButton
    private lateinit var service: ServiceRequest

    //
    private lateinit var userRating: TextView
    private lateinit var totalJobs: TextView

    companion object {
        var serviceToBeOrdered: ServiceRequest? = null
        var userUidForFragment: String? = null
        var viewOnlyMode: Boolean = false
    }

    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_specific_request)

        // Retrieve the ServiceRequest object from the intent extras
        val serviceRequest = intent.getParcelableExtra<ServiceRequest>("ServiceRequest")

        // Maps Fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Get the Order object from the intent extras
        val orderExtra = intent.getParcelableExtra<Order>("orderPassed") as? Order
        if (orderExtra != null) {
            order = orderExtra
            Log.d("DisplaySpecificRequest", "Order retrieved successfully: $order")
        } else {
            order = Order()
            Log.d("DisplaySpecificRequest", "No Order found in intent extras. Using default Order.")
        }

        val serviceExtra = intent.getParcelableExtra<ServiceRequest>("ServiceRequest")
        if (serviceExtra != null) {
            service = serviceExtra ?: ServiceRequest()
            Log.d("DisplaySpecificRequest", "ServiceRequest retrieved successfully: $service")
        } else {
            Log.d("DisplaySpecificRequest", "No ServiceRequest found in intent extras.")
            // Handle the case when serviceExtra is null
        }

        serviceToBeOrdered = service

        // Log the ServiceRequest object
        Log.d("DisplaySpecificRequest", "ServiceRequest: $serviceToBeOrdered")

        // Use the ServiceRequest object to populate your views
        if (serviceRequest != null) {
            serviceTitleTextView = findViewById(R.id.tvServiceTitle)
            serviceDescriptionTextView = findViewById(R.id.tvServiceDescription)
            serviceCategoryTextView = findViewById(R.id.tvServiceCategory)
            priceTextView = findViewById(R.id.tvPriceService)
            dateTextView = findViewById(R.id.date_orderDetails)
            timePicker = findViewById(R.id.time_orderDetails)
            addressTextView = findViewById(R.id.address_fragmentBottomBookingDetails)
//            dateAndTimeTextView = findViewById(R.id.dateAndTimeOrdered_orderDetails)
//            contactNumber = findViewById(R.id.number_orderDetails)
            modeOfPayment = findViewById(R.id.mode_orderDetails)

            // Set the text of your TextViews
            serviceTitleTextView.text = service.title
            serviceDescriptionTextView.text = service.description
            serviceCategoryTextView.text = service.category
            priceTextView.text = service.price.toString()
            dateTextView.text = service.date
            timePicker.text = service.time
            addressTextView.text = service.address
//            dateAndTimeTextView.text = service.dateBooked
//            contactNumber.text = service.contactNumber
            modeOfPayment.text = service.modeOfPayment
        } else {
            Log.d("DisplaySpecificRequest", "ServiceRequest is null")
        }



        //intents
        serviceToBeOrdered = service
        userUid = service.userUid!!
        serviceUid = service.uid!!
        //Map everything here
//        userReviewButton = findViewById(R.id.btnViewReviews)
        toolBar = findViewById(R.id.tbService)
//        showProfileImageBtn = findViewById(R.id.ibProfileDropdown)

        assistButton = findViewById(R.id.btnCreateOrderService)
        profileImageView = findViewById(R.id.ivProfileImage)
        nameTextView = findViewById(R.id.tvNameService)
        floatingActionButton = findViewById(R.id.fabMessageSeller)
//        userRating = findViewById(R.id.tvRatingService)
//        totalJobs = findViewById(R.id.tvJobsAccomplishedService)


        //Temporary Comments
//        dateTextView.text = "${dateTextView.text} ${order.date}"
//        timePicker.text = "${timePicker.text} ${order.time}"
//        priceTextView.text = "${priceTextView.text} ${order.price.toString()}"
//        serviceCategoryTextView.text = "${serviceCategoryTextView.text} ${order.category}"
//        serviceTitleTextView.text = "${serviceTitleTextView.text} ${order.title}"
//        serviceDescriptionTextView.text = "${serviceDescriptionTextView.text} ${order.description}"
//        dateAndTimeTextView.text = "Date and Time Booked: ${convertLongToDate(order.dateOrdered)}"
//        addressTextView.text = "${addressTextView.text} ${order.address}"
//        modeOfPayment.text = "Mode of Payment: ${order.modeOfPayment}"



//        userReviewButton.setOnClickListener {
//            val intent = Intent(this, DisplayReviewsActivity::class.java)
//            intent.putExtra("userUid", userUid)
//            startActivity(intent)
//        }

        toolBar.setNavigationOnClickListener {
            finish()
        }
        floatingActionButton.setOnClickListener {
            goToChatLogActivity()
        }
        assistButton.setOnClickListener {

            createTheOrder()

        }
        //show Profile infos
//        showProfileImageBtn.setOnClickListener {
//            userUidForFragment = userUid
//            val profileFragment = BottomFragmentShowProfile()
//            profileFragment.show(supportFragmentManager, "TAG")
//
//        }
        fetchUserData()
        fetchService()
        checkIfViewMode()
//        showUserReviewRatingAndJobsFinished()


    }

    private fun createTheOrder() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserUid = currentUser.uid
            val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$currentUserUid")
            val assistToRef = FirebaseDatabase.getInstance().getReference("booked_by/assistConfirmation")

            serviceToBeOrdered?.let { serviceRequest ->
                val requestUserUid = serviceRequest.bookedBy
                val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/${serviceRequest.userUid}")
                val userRef = FirebaseDatabase.getInstance().getReference("users/$currentUserUid")

                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val orderId = assistToRef.push().key ?: return
                            val serviceUid = serviceRequest.uid
                            val order = Order(
                                uid = orderId,
                                service_booked_uid = serviceUid,
                                address = addressTextView.text.toString(),
                                date = dateTextView.text.toString(),
                                name = "${user.firstName} ${user.lastName}",
                                price = serviceRequest.price,
                                time = serviceRequest.time,
                                title = serviceRequest.title,
                                category = serviceRequest.category,
                                description = serviceRequest.description,
                                modeOfPayment = modeOfPayment.text.toString(),
                                bookedBy = requestUserUid,
                                bookedTo = currentUserUid,
                                assistUser = orderId,
                                assistConfirmation = "TRUE"
                            )

                            // Update both booked_by and booked_to nodes atomically
                            val updates = hashMapOf<String, Any>(
                                "/booked_by/${serviceRequest.bookedBy}/$serviceUid/$currentUserUid" to order,
                                "/booked_to/$currentUserUid/$serviceUid" to order
                            )

                            FirebaseDatabase.getInstance().reference.updateChildren(updates).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("DisplaySpecificRequest", "Order created and nodes updated successfully.")
                                    // Handle the successful order creation here
                                } else {
                                    Log.e("DisplaySpecificRequest", "Error updating nodes: ${task.exception}")
                                    // Handle the error here
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle the error here
                    }
                })
            }

            // Increment notifications count for the current user
            val notificationsRef = FirebaseDatabase.getInstance().getReference("/notifications/$currentUserUid")
            notificationsRef.runTransaction(object : com.google.firebase.database.Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): com.google.firebase.database.Transaction.Result {
                    val currentCount = mutableData.getValue(Int::class.java) ?: 0
                    mutableData.value = currentCount + 1
                    return com.google.firebase.database.Transaction.success(mutableData)
                }

                override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                    if (error != null) {
                        Log.e("DisplaySpecificRequest", "Error incrementing notifications: ${error.message}")
                    }
                }
            })

            val intent = Intent().apply {
                putExtra("ServiceRequest", serviceToBeOrdered)
            }

            setResult(RESULT_OK, intent)
        }

        finish()
    }


//    private fun getTheTime(): String {
//        var output: String
//        var hr = 0
//        var minute = ""
//        var text = ""
//
//        if (timePicker.hour >= 13) {
//            hr = timePicker.hour - 12
//            text = "PM"
//        } else if (timePicker.hour == 12) {
//            hr = timePicker.hour
//            text = "PM"
//        } else {
//            hr = timePicker.hour
//            text = "AM"
//        }
//
//        if (timePicker.minute <= 9) {
//            minute = "0${timePicker.minute}"
//        } else {
//            minute = "${timePicker.minute}"
//        }
//
//        output = "$hr:$minute $text"
//        return output
//
//
//    }

    private fun convertLongToDate(long: Long): String {
        val resultdate = Date(long)
        return resultdate.toString()
    }

    private fun showUserReviewRatingAndJobsFinished() {
        val serviceProviderUid = service.userUid!!
        val ref = FirebaseDatabase.getInstance().getReference("user_seller_info/$serviceProviderUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserSellerInfo::class.java)!!
                if (userData.count == 0) {
                    userRating.text = "No ratings yet."
                } else {
                    val ttlrtng = userData.totalRating!!.toDouble()
                    val ttlcnt = userData.count!!.toDouble()
                    val rating = (ttlrtng / ttlcnt)
                    val solution = Math.round(rating * 10.0) / 10.0
                    userRating.text = "$solution/5"
                }
                totalJobs.text = "Jobs Completed: ${userData.totalJobsFinished}"

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun checkIfViewMode() {
        if (viewOnlyMode) {
            assistButton.isEnabled = false
            floatingActionButton.isEnabled = false
            val snackbar = Snackbar.make(findViewById(R.id.container_activityDisplaySpecificService), "View-Only Mode", Snackbar.LENGTH_INDEFINITE)
            snackbar.show()

            return
        }
        if (!viewOnlyMode) {
            assistButton.isEnabled = true
            floatingActionButton.isEnabled = true
        }
    }


    private fun goToChatLogActivity() {
        val intent = Intent(applicationContext, ChatLogActivity::class.java)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun fetchService() {
        val ref = FirebaseDatabase.getInstance().getReference("/services/${userUid}/${serviceUid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val service = p0.getValue(Service::class.java)
                if (service != null) {
                    serviceTitleTextView.text = "${service.title?.toUpperCase()}"
                    serviceDescriptionTextView.text = "${service.description}"
                    serviceCategoryTextView.text = "Category: ${service.category}"
                    priceTextView.text = "Rp ${service.price}"
                    assistButton.text = assistButton.text.toString() + " (Rp ${service.price})"
                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }


    private fun fetchUserData() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/${userUid}")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                //Picasso is a caching manager for the image
                Picasso.get().load(user!!.profileImageUrl).into(profileImageView)
                nameTextView.text = "${user.firstName.toString()} ${user.lastName.toString()}, ${user.age}"
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Get latitude and longitude from the ServiceRequest object
        val latitude = service.latitude ?: 0.0
        val longitude = service.longitude ?: 0.0

        // Check if both latitude and longitude are 0.0
        if (latitude == 0.0 && longitude == 0.0) {
            // Hide the map fragment
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            if (mapFragment != null) {
                supportFragmentManager.beginTransaction().hide(mapFragment).commit()
            }
        } else {
            // Create a LatLng object using the latitude and longitude
            val userLocation = LatLng(latitude, longitude)

            // Add a marker at the user's location and move the camera
            mMap.addMarker(MarkerOptions().position(userLocation).title("Marker in User Location"))

            val zoomLevel = 16.0f
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel))
        }
    }
}
