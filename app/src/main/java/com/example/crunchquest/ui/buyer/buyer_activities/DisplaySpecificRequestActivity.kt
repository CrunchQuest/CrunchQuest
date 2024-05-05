@file:Suppress("DEPRECATION")

package com.example.crunchquest.ui.buyer.buyer_activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.crunchquest.R
import com.example.crunchquest.data.model.Order
import com.example.crunchquest.data.model.Service
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.data.model.User
import com.example.crunchquest.data.model.UserSellerInfo
import com.example.crunchquest.ui.messages.ChatLogActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.Date


class DisplaySpecificRequestActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var toolBar: Toolbar

    private lateinit var userUid: String
    private lateinit var serviceUid: String

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

        service = (intent.getParcelableExtra("ServiceRequest") as? ServiceRequest)!!
        Log.d("DisplaySpecificRequest", "ServiceRequest is not null")

        serviceToBeOrdered = service

        // Log the ServiceRequest object
        Log.d("DisplaySpecificRequest", "ServiceRequest: $serviceToBeOrdered")

        // Use the ServiceRequest object to populate your views
        serviceTitleTextView = findViewById(R.id.tvServiceTitle)
        serviceDescriptionTextView = findViewById(R.id.tvServiceDescription)
        serviceCategoryTextView = findViewById(R.id.tvServiceCategory)
        priceTextView = findViewById(R.id.tvPriceService)

        // Set the text of your TextViews
        serviceTitleTextView.text = service.title
        serviceDescriptionTextView.text = service.description
        serviceCategoryTextView.text = service.category
        priceTextView.text = service.price.toString()

        //intents
        serviceToBeOrdered = service
        userUid = service.userUid!!
        serviceUid = service.uid!!
        //Map everything here
//        userReviewButton = findViewById(R.id.btnViewReviews)
        toolBar = findViewById(R.id.tbService)
//        showProfileImageBtn = findViewById(R.id.ibProfileDropdown)
        serviceTitleTextView = findViewById(R.id.tvServiceTitle)
        serviceDescriptionTextView = findViewById(R.id.tvServiceDescription)
        serviceCategoryTextView = findViewById(R.id.tvServiceCategory)
        priceTextView = findViewById(R.id.tvPriceService)
        assistButton = findViewById(R.id.btnCreateOrderService)
        profileImageView = findViewById(R.id.ivProfileImage)
        nameTextView = findViewById(R.id.tvNameService)
        floatingActionButton = findViewById(R.id.fabMessageSeller)
//        userRating = findViewById(R.id.tvRatingService)
//        totalJobs = findViewById(R.id.tvJobsAccomplishedService)
        dateTextView = findViewById(R.id.date_orderDetails)
        timePicker = findViewById(R.id.time_orderDetails)
        addressTextView = findViewById(R.id.address_fragmentBottomBookingDetails)
        dateAndTimeTextView = findViewById(R.id.dateAndTimeOrdered_orderDetails)
        contactNumber = findViewById(R.id.number_orderDetails)
        modeOfPayment = findViewById(R.id.mode_orderDetails)


        dateTextView.text = "${dateTextView.text} ${order.date}"
        timePicker.text = "${timePicker.text} ${order.time}"
        priceTextView.text = "${priceTextView.text} ${order.price.toString()}"
        serviceCategoryTextView.text = "${serviceCategoryTextView.text} ${order.category}"
        serviceTitleTextView.text = "${serviceTitleTextView.text} ${order.title}"
        serviceDescriptionTextView.text = "${serviceDescriptionTextView.text} ${order.description}"
        dateAndTimeTextView.text = "Date and Time Booked: ${convertLongToDate(order.dateOrdered)}"
        addressTextView.text = "${addressTextView.text} ${order.address}"
        modeOfPayment.text = "Mode of Payment: ${order.modeOfPayment}"



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
            val price = service.price ?: 0
            val bottomFragment = BottomFragmentAssist.newInstance(price)
            bottomFragment.show(supportFragmentManager, "TAG")
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
        imageSlider()
        checkIfViewMode()
//        showUserReviewRatingAndJobsFinished()


    }

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

    private fun imageSlider() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val imageSlider: ImageSlider = findViewById(R.id.isService)
        val remoteImages: ArrayList<SlideModel> = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("/Sliders/$serviceUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    remoteImages.add(SlideModel(it.child("url").value.toString(), "", ScaleTypes.CENTER_CROP))
                }
                imageSlider.setImageList(remoteImages, ScaleTypes.CENTER_CROP)
                imageSlider.stopSliding()
                imageSlider.setItemClickListener(object : ItemClickListener {
                    override fun onItemSelected(position: Int) {
//                        var showPictureFragment = ShowPictureFragment()
//                        showPictureFragment.show(supportFragmentManager, "TAG")
//                        ShowPictureFragment.serviceUid = serviceUid
                        Log.d("POSITIONTRYLANG", "$position")
                        val url = remoteImages[position].imageUrl!!
                        val imagePopup = ImagePopup(this@DisplaySpecificRequestActivity)
                        imagePopup.initiatePopupWithPicasso(url)
                        imagePopup.viewPopup()
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        imageSlider.stopSliding()

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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

}
