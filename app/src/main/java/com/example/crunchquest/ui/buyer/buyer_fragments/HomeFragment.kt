package com.example.crunchquest.ui.buyer.buyer_fragments

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.TooltipCompat
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.crunchquest.R
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.data.model.User
import com.example.crunchquest.data.model.UserLocation
import com.example.crunchquest.data.model.convertToServiceRequest
import com.example.crunchquest.data.network.ApiConfig
import com.example.crunchquest.data.network.ApiService
import com.example.crunchquest.data.network.response.ServiceRequestResponse
import com.example.crunchquest.ui.adapter.SliderAdapter
import com.example.crunchquest.ui.buyer.BuyerActivity
import com.example.crunchquest.ui.buyer.bottomNavigationBuyer
import com.example.crunchquest.ui.buyer.buyer_activities.DisplaySpecificRequestActivity
import com.example.crunchquest.ui.buyer.buyer_activities.RequestActivity
import com.example.crunchquest.ui.buyer.buyer_activities.ServiceCategoryActivity
import com.example.crunchquest.ui.components.groupie_views.OrderItem
import com.example.crunchquest.ui.components.groupie_views.ServiceCategoryItem
import com.example.crunchquest.ui.components.groupie_views.ServiceRequestItem
import com.example.crunchquest.ui.general.LoginActivity
import com.example.crunchquest.ui.general.ProfileSettingsActivity
import com.example.crunchquest.ui.messages.MessagesActivity
import com.example.crunchquest.utility.handlers.ServiceRequestHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.coroutines.launch
import retrofit2.HttpException


class HomeFragment : Fragment() {

    private lateinit var mainFab: FloatingActionButton
    private lateinit var subFab1: FloatingActionButton
    private lateinit var subFab2: FloatingActionButton
    private lateinit var subFab3: FloatingActionButton
    private lateinit var fabHandler: Handler
//    private lateinit var longClickRunnable: Runnable
    private var isExpanded = false

    // Maps
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Service Request
    lateinit var serviceRequestRecyclerView: RecyclerView
    private lateinit var serviceRequestArrayList: ArrayList<ServiceRequest>
    private var serviceRequestHandler = ServiceRequestHandler()
    var currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

    // Add a variable to store the clicked item
    var clickedServiceRequestItem: ServiceRequestItem? = null

    private lateinit var v: View
    private lateinit var categoryRecyclerView: RecyclerView
    val adapterCategory = GroupAdapter<ViewHolder>().apply {
        spanCount = 2
    }
    private val i = adapterCategory.spanSizeLookup

    val adapterRequest = GroupAdapter<ViewHolder>().apply {
        spanCount = 1
    }


    val searchFragment = SearchFragment()

    // Network
    private val apiService: ApiService by lazy {
        ApiConfig.retrofit.create(ApiService::class.java)
    }
    private var userPreferences: List<Int> = emptyList()
    private var serviceRequests: List<ServiceRequestResponse> = emptyList()

    companion object {
        val SERVICECATEGORY = "serviceCategory"
        const val REQUEST_CODE = 1
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar: Toolbar = v.findViewById(R.id.my_toolbar)
        (activity as AppCompatActivity).supportActionBar?.hide()

        // Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        val circleImageView: ImageView = toolbar.findViewById(R.id.circleImageView)

        circleImageView.setOnClickListener {
            // Handle click event here
            val popupMenu = PopupMenu(v.context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)

            // Remove unnecessary items
            popupMenu.menu.removeItem(R.id.addService)
//            popupMenu.menu.removeItem(R.id.search)
            popupMenu.menu.removeItem(R.id.message)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.profileSettings -> {
                        val intent = Intent(requireContext(), ProfileSettingsActivity::class.java)
                        intent.putExtra("intent", "buyer")
                        startActivity(intent)
                    }
//                    R.id.changeMode -> {
//                        startActivity(Intent(requireContext(), ChooseActivity::class.java))
//                    }
                    R.id.logOut -> {
                        showDialogFun()
                    }
                }
                true
            }
            popupMenu.show()
        }

        val messageIv = toolbar.findViewById<ImageView>(R.id.message_iv)

        messageIv.setOnClickListener {
            val intent = Intent(requireContext(), MessagesActivity::class.java)
            startActivity(intent)
        }


        setupBanner()

        // Get the user ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")

        if (userId != null) {
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    Picasso.get().load(user!!.profileImageUrl).into(circleImageView)
                }

                override fun onCancelled(p0: DatabaseError) {
                    circleImageView.setImageResource(R.drawable.ic_person)
                }
            })
        }

        //Map everything here
        setupCategoryRv()

//        val btn = v.findViewById<Button>(R.id.searchBtn)
//        //Button onclick listener
//        btn.setOnClickListener {
//            val transaction = activity?.supportFragmentManager?.beginTransaction()
//            transaction?.replace(R.id.wrapper, searchFragment)
//            transaction?.commit()
//        }

        // Quest RecycleView
        serviceRequestRecyclerView =
            v.findViewById(R.id.serviceRequestRecyclerView_activityBuyersRequest)
        serviceRequestArrayList = ArrayList()

        //Floating action button
        setupFab()


        checkUserLocation()

        mainFab.setOnClickListener {
            if (isExpanded) {
                collapseSubFabs()
            } else {
                // Check if the location permission has been granted
                if (ActivityCompat.checkSelfPermission(
                        v.context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        v.context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                } else {
                    // Navigate to RequestActivity
                    val intent = Intent(v.context, RequestActivity::class.java)
                    startActivity(intent)
                }
            }
        }


        fetchServiceCategory()
        //Gets all the Service Requests available
        serviceRequestRecyclerView.adapter = adapterRequest

        // TO USE THE API vvv
        fetchServiceRequestsApi()

//        fetchServiceRequests()
        return v
    }

    private fun setupBanner() {
        val images = listOf(
            R.drawable.banner_1,
            R.drawable.banner_2
        )
        val viewPager = v.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = SliderAdapter(images)

        val tabLayout = v.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }

    private fun setupFab() {
        mainFab = v.findViewById(R.id.mainFab)
//        subFab1 = v.findViewById(R.id.subFab1)
//        subFab2 = v.findViewById(R.id.subFab2)
//        subFab3 = v.findViewById(R.id.subFab3)
        fabHandler = Handler()
        mainFab.elevation = 8f
        val animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fab_scale_up)
        mainFab.startAnimation(animation)

//        longClickRunnable = Runnable {
//            expandSubFabs()
//        }
//
        mainFab.setOnLongClickListener {
//            fabHandler.postDelayed(longClickRunnable, 1000)
            TooltipCompat.setTooltipText(mainFab, "Add a new request")
            true
        }

        mainFab.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                // Cancel the long click handler if the button is released before 3 seconds
//                fabHandler.removeCallbacks(longClickRunnable)
            }
            false
        }

//        subFab1.setOnClickListener {
//            Toast.makeText(context, "Example of sub fab 1", Toast.LENGTH_SHORT).show()
//        }
//        subFab2.setOnClickListener {
//            Toast.makeText(context, "Example of sub fab 2", Toast.LENGTH_SHORT).show()
//        }
//        subFab3.setOnClickListener {
//            Toast.makeText(context, "Example of sub fab 3", Toast.LENGTH_SHORT).show()
//        }
    }

    //Check if user has allow the location fror pushing the last/current location to database
    private fun checkUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                v.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                v.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission is granted
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity()) { location ->
                    if (location != null) {
                        pushLocation(location)
                    } else {
                        Log.e("TAG", "Location is null")
                    }
                }
        }
    }

    private fun pushLocation(location: Location) {
        val currentLocation = FirebaseDatabase.getInstance().getReference("user_current_location/${currentUserUid}")
        currentLocation.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userLocation = snapshot.getValue(UserLocation::class.java) ?: UserLocation()
                userLocation.latitude = location.latitude
                userLocation.longitude = location.longitude
                userLocation.timestamp = System.currentTimeMillis()

                currentLocation.setValue(userLocation)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Add this method to your HomeFragment class
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the ServiceRequest object or its ID
            val serviceRequest = data?.getParcelableExtra("ServiceRequest") as? ServiceRequest
            if (serviceRequest != null) {
                // Remove the ServiceRequest from adapterRequest
                var serviceRequestItem: ServiceRequestItem? = null
                for (i in 0 until adapterRequest.itemCount) {
                    val item = adapterRequest.getItem(i)
                    if (item is ServiceRequestItem && item.serviceRequest == serviceRequest) {
                        serviceRequestItem = item
                        break
                    }
                }

                if (serviceRequestItem != null) {
                    adapterRequest.remove(serviceRequestItem)
                    serviceRequestArrayList.remove(serviceRequestItem.serviceRequest) // remove from the data source
                    adapterRequest.notifyDataSetChanged() // notify the adapter about the data change
                }
            }
        }
    }

//    private fun fetchServiceRequests() {
//        serviceRequestHandler.serviceRequestRef.addListenerForSingleValueEvent(object :
//            ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                adapterRequest.clear()
//                p0.children.forEach {
//                    val serviceRequest = it.getValue(ServiceRequest::class.java)
//                    if (serviceRequest!!.userUid != currentUserUid) {
//                        adapterRequest.add(ServiceRequestItem(serviceRequest))
//                    }
//                }
//                serviceRequestRecyclerView.adapter = adapterRequest
//
//                // Modify the ServiceRequestItem click listener
//                adapterRequest.setOnItemClickListener { item, view ->
//                    val serviceRequestItem = item as ServiceRequestItem
//                    clickedServiceRequestItem = serviceRequestItem
//                    val intent = Intent(view.context, DisplaySpecificRequestActivity::class.java)
//                    intent.putExtra("ServiceRequest", serviceRequestItem.serviceRequest)
//                    startActivityForResult(intent, REQUEST_CODE)
//
//                    // Add a log message
//                    Log.d("ServiceRequest", "Clicked on service request: ${serviceRequestItem.serviceRequest}")
//
//
//                }

//                // Add a method to add an OrderItem to the adapter
//                fun addOrderItem(orderItem: OrderItem) {
//                    adapterRequest.add(orderItem)
//                }
//
////                hideOrShowViews()
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
////    }


    private fun showDialogFun() {
        //Dialog before sign out
        val dialogBuilder = AlertDialog.Builder(requireContext())
        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to sign out?")
            // if the dialog is cancelable
            .setCancelable(true)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { _, _ ->
                var auth = FirebaseAuth.getInstance()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                BuyerActivity.currentUser = null
                activity?.finish()
                Toast.makeText(requireContext(), "Signed out", Toast.LENGTH_LONG).show()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Sign Out")
        // show alert dialog
        alert.show()
    }

    private fun fetchServiceCategory() {
        adapterCategory.clear()
        //Get the array of service category
        val arrayList = ArrayList(listOf(*resources.getStringArray(R.array.services_category)))
        //Add the different categories to the adapter
        adapterCategory.add(ServiceCategoryItem(arrayList[0], R.drawable.services_computer_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[1], R.drawable.services_homecleaning_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[2], R.drawable.services_plumbing_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[3], R.drawable.services_electrical_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[4], R.drawable.services_moving_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[5], R.drawable.services_delivery_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[6], R.drawable.services_aircon_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[7], R.drawable.services_homerepair_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[8], R.drawable.services_auto_alt))
        //Attach the adapter to the recycler view
        categoryRecyclerView.adapter = adapterCategory

        adapterCategory.setOnItemClickListener { item, _ ->
            val intent = Intent(v.context, ServiceCategoryActivity::class.java)
            val category = item as ServiceCategoryItem
            Log.d("ServiceCategoryTitle", category.serviceTitle)
            intent.putExtra(SERVICECATEGORY, category.serviceTitle)
            startActivity(intent)

        }
    }

    private fun setupCategoryRv() {
        categoryRecyclerView = v.findViewById(R.id.recyclerView_fragmentHome)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.layoutManager = layoutManager
        categoryRecyclerView.addItemDecoration(CustomItemDecoration(8))
    }

    override fun onResume() {
        super.onResume()
//        (activity as BuyerActivity?)?.setActionBarTitle("Services")
        (activity as BuyerActivity?)?.supportActionBar?.hide()
        bottomNavigationBuyer.menu.findItem(R.id.homePage).isChecked = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Remove callbacks to prevent memory leaks
//        fabHandler.removeCallbacks(longClickRunnable)

        (activity as AppCompatActivity).supportActionBar?.show()
    }

    private fun expandSubFabs() {
        subFab1.visibility = View.VISIBLE
        subFab2.visibility = View.VISIBLE
        subFab3.visibility = View.VISIBLE
        isExpanded = true
    }

    private fun collapseSubFabs() {
        subFab1.visibility = View.GONE
        subFab2.visibility = View.GONE
        subFab3.visibility = View.GONE
        isExpanded = false
    }

    // REQUEST FROM API
    private fun fetchServiceRequestsApi() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            lifecycleScope.launch {
                try {
                    // Fetch ranked service requests with distances
                    val serviceRequestsResponse = apiService.getServiceRequests(userId)
                    val serviceRequests = serviceRequestsResponse.map { convertToServiceRequest(it) }
                    val distances = serviceRequestsResponse.map { it.distance } // Assuming 'distance' is a property in ServiceRequestResponse
                    updateRecyclerView(serviceRequests, distances)
                } catch (e: HttpException) {
                    Log.e("HomeFragment", "HTTP error: ${e.message()}")
                } catch (e: Exception) {
                    Log.e("HomeFragment", "Error fetching data: $e")
                }
            }
        } else {
            Log.e("HomeFragment", "User ID is null")
        }
    }


    private fun updateRecyclerView(serviceRequests: List<ServiceRequest>, distances: List<Double>) {
        adapterRequest.clear()
        serviceRequests.forEachIndexed { index, serviceRequest ->
            val distance = distances.getOrNull(index) ?: Double.MAX_VALUE
            adapterRequest.add(ServiceRequestItem(serviceRequest, distance, requireContext()))
            Log.d("ServiceRequest", "ServiceRequest from fetchServiceRequest: $serviceRequest")
        }
        serviceRequestRecyclerView.adapter = adapterRequest

        // Modify the ServiceRequestItem click listener
        adapterRequest.setOnItemClickListener { item, view ->
            // Handle item click as before
            val serviceRequestItem = item as ServiceRequestItem
            clickedServiceRequestItem = serviceRequestItem
            val intent = Intent(view.context, DisplaySpecificRequestActivity::class.java)
            intent.putExtra("ServiceRequest", serviceRequestItem.serviceRequest)
            startActivityForResult(intent, REQUEST_CODE)

            // Add a log message
            Log.d("ServiceRequest", "Clicked on service request: ${serviceRequestItem.serviceRequest}")
        }

        // Add a method to add an OrderItem to the adapter
        fun addOrderItem(orderItem: OrderItem) {
            adapterRequest.add(orderItem)
        }
    }

}

class CustomItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.top = space
        outRect.bottom = space

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space
        }
    }
}


