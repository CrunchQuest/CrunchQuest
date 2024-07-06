package com.crunchquest.android.ui.buyer.buyer_fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.android.R
import com.crunchquest.android.data.model.ServiceRequest
import com.crunchquest.android.data.model.UserLocation
import com.crunchquest.android.data.network.ApiConfig
import com.crunchquest.android.data.network.ApiService
import com.crunchquest.android.databinding.FragmentHomeBinding
import com.crunchquest.android.repository.HomeRepository
import com.crunchquest.android.ui.adapter.SliderAdapter
import com.crunchquest.android.ui.buyer.BuyerActivity
import com.crunchquest.android.ui.buyer.bottomNavigationBuyer
import com.crunchquest.android.ui.buyer.buyer_activities.DisplaySpecificRequestActivity
import com.crunchquest.android.ui.buyer.buyer_activities.RequestActivity
import com.crunchquest.android.ui.buyer.buyer_activities.ServiceCategoryActivity
import com.crunchquest.android.ui.components.groupie_views.OrderItem
import com.crunchquest.android.ui.components.groupie_views.ServiceCategoryItem
import com.crunchquest.android.ui.components.groupie_views.ServiceRequestItem
import com.crunchquest.android.ui.general.LoginActivity
import com.crunchquest.android.ui.general.ProfileSettingsActivity
import com.crunchquest.android.ui.general.SendRequirementActivity
import com.crunchquest.android.ui.messages.MessagesActivity
import com.crunchquest.android.viewmodel.HomeViewModel
import com.crunchquest.android.viewmodel.HomeViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentUserUid: String
    private var clickedServiceRequestItem: ServiceRequestItem? = null
    private var isExpanded = false
    private lateinit var categoryRecyclerView: RecyclerView
    private val adapterCategory = GroupAdapter<ViewHolder>().apply {
        spanCount = 2
    }
    private val adapterRequest = GroupAdapter<ViewHolder>().apply {
        spanCount = 1
    }

    companion object {
        const val SERVICECATEGORY = "serviceCategory"
        const val REQUEST_CODE = 1
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize ViewModel with repository
        val apiService = ApiConfig.retrofit.create(ApiService::class.java)
        val repository = HomeRepository(apiService)
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        checkUserLocation()
        setupUI()
        observeViewModel()
        checkUserActiveRequest()

        return view
    }

    private fun setupUI() {
        // Toolbar setup
        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.myToolbar.circleImageView.setOnClickListener { showPopupMenu(it) }
        binding.myToolbar.messageIv.setOnClickListener {
            startActivity(Intent(requireContext(), MessagesActivity::class.java))
        }

        setupBanner()
        setupFab()
//        setupCategoryRecyclerView()

        // Fetch data
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            homeViewModel.fetchUserProfileImage(it)
            homeViewModel.fetchServiceRequests(it)
        }
    }

    private fun observeViewModel() {
        homeViewModel.userProfileImageUrl.observe(viewLifecycleOwner, Observer { imageUrl ->
            if (imageUrl.isNotEmpty()) {
                Picasso.get().load(imageUrl).into(binding.myToolbar.circleImageView)
            } else {
                binding.myToolbar.circleImageView.setImageResource(R.drawable.ic_person)
            }
        })

        homeViewModel.serviceRequests.observe(viewLifecycleOwner, Observer { serviceRequests ->
            updateRecyclerView(serviceRequests, emptyList(), emptyList())
            checkIfUserHasActiveRequest(serviceRequests)
        })
    }

    private fun checkUserActiveRequest() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            homeViewModel.fetchServiceRequests(userId)
        } else {
            Log.e("HomeFragment", "User ID is null. Unable to fetch service requests.")
        }
    }

    private fun checkIfUserHasActiveRequest(serviceRequests: List<ServiceRequest>) {
        val userHasActiveRequest = serviceRequests.any { it.userUid == currentUserUid && !it.reviewed!! }
        Log.d("HomeFragment", "User has active request: $userHasActiveRequest")

        binding.mainFab.isEnabled = true
        binding.mainFab.setOnClickListener {
            if (userHasActiveRequest) {
                // If the user has an active request, show a toast message
                Toast.makeText(requireContext(), "You must complete your current request first.", Toast.LENGTH_LONG).show()
            } else {
                // If the user does not have an active request, proceed with the intended action
                setupFab()
            }
        }
    }


    private fun updateRecyclerView(
        serviceRequests: List<ServiceRequest>,
        distances: List<Double>,
        similarity_scores: List<Int>
    ) {
        adapterRequest.clear()
        val filteredRequests = serviceRequests.filter { it.userUid != currentUserUid }
        filteredRequests.forEachIndexed { index, serviceRequest ->
            val distance = distances.getOrNull(index) ?: Double.MAX_VALUE
            val similarity = similarity_scores.getOrNull(index) ?: 0
            Log.d("ServiceRequest", "Distance: $distance")
            Log.d("ServiceRequest", "Similarity: $similarity")
            adapterRequest.add(
                ServiceRequestItem(
                    serviceRequest,
                    distance,
                    similarity,
                    requireContext()
                )
            )
            Log.d("ServiceRequest", "ServiceRequest from fetchServiceRequest: $serviceRequest")
        }
        binding.serviceRequestRecyclerViewActivityBuyersRequest.adapter = adapterRequest

        // Modify the ServiceRequestItem click listener
        adapterRequest.setOnItemClickListener { item, view ->
            // Handle item click as before
            val serviceRequestItem = item as ServiceRequestItem
            clickedServiceRequestItem = serviceRequestItem
            val intent = Intent(view.context, DisplaySpecificRequestActivity::class.java)
            intent.putExtra("ServiceRequest", serviceRequestItem.serviceRequest)
            startActivityForResult(intent, REQUEST_CODE)

            // Add a log message
            Log.d(
                "ServiceRequest",
                "Clicked on service request: ${serviceRequestItem.serviceRequest}"
            )
        }

        // Add a method to add an OrderItem to the adapter
        fun addOrderItem(orderItem: OrderItem) {
            adapterRequest.add(orderItem)
        }
    }

    private fun setupBanner() {
        val images = listOf(
            R.drawable.banner_1,
            R.drawable.banner_2
        )
        binding.viewPager.adapter = SliderAdapter(images)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
    }

    private fun setupFab() {
        binding.mainFab.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val verifiedClient =
                        snapshot.child("verifiedClient").getValue(String::class.java)
                    if (verifiedClient == "NOT_VERIFIED") {
                        if (isExpanded) {
                            collapseSubFabs()
                        } else {
                            Toast.makeText(
                                context,
                                "Please verify your account to proceed",
                                Toast.LENGTH_SHORT
                            ).show()
                            expandSubFabs()
                        }

                    } else if (verifiedClient == "VERIFIED") {
                        collapseSubFabs()
                        // Check if the location permission has been granted
                        if (context?.let { it1 ->
                                ActivityCompat.checkSelfPermission(
                                    it1,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            } != PackageManager.PERMISSION_GRANTED && context?.let { it1 ->
                                ActivityCompat.checkSelfPermission(
                                    it1,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            } != PackageManager.PERMISSION_GRANTED
                        ) {
                            // Permission is not granted, request it
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                LOCATION_PERMISSION_REQUEST_CODE
                            )
                        } else {
                            // Navigate to RequestActivity
                            val intent = Intent(context, RequestActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })
        }

        binding.subFab1.setOnClickListener {
            val intent = Intent(context, SendRequirementActivity::class.java)
            startActivity(intent)
        }

    binding.subFab1.setOnClickListener {
            val intent = Intent(context, SendRequirementActivity::class.java)
            startActivity(intent)
        }
    }


    private fun expandSubFabs() {
        binding.subFab1.visibility = View.VISIBLE
        isExpanded = true
    }

    private fun collapseSubFabs() {
        binding.subFab1.visibility = View.GONE
        isExpanded = false
    }

//    private fun setupCategoryRecyclerView() {
//        categoryRecyclerView = binding.recyclerViewFragmentHome
//        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
//        categoryRecyclerView.layoutManager = layoutManager
//        categoryRecyclerView.addItemDecoration(CustomItemDecoration(8))
//        fetchServiceCategory()
//    }

    private fun fetchServiceCategory() {
        adapterCategory.clear()
        val arrayList = ArrayList(listOf(*resources.getStringArray(R.array.services_category)))
        adapterCategory.add(ServiceCategoryItem(arrayList[0], R.drawable.services_computer_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[1], R.drawable.services_homecleaning_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[2], R.drawable.services_plumbing_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[3], R.drawable.services_electrical_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[4], R.drawable.services_moving_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[5], R.drawable.services_delivery_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[6], R.drawable.services_aircon_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[7], R.drawable.services_homerepair_alt))
        adapterCategory.add(ServiceCategoryItem(arrayList[8], R.drawable.services_auto_alt))
        categoryRecyclerView.adapter = adapterCategory

        adapterCategory.setOnItemClickListener { item, _ ->
            val intent = Intent(requireContext(), ServiceCategoryActivity::class.java)
            val category = item as ServiceCategoryItem
            intent.putExtra(SERVICECATEGORY, category.serviceTitle)
            startActivity(intent)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)

        popupMenu.menu.removeItem(R.id.addService)
        popupMenu.menu.removeItem(R.id.message)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profileSettings -> {
                    val intent = Intent(requireContext(), ProfileSettingsActivity::class.java)
                    intent.putExtra("intent", "buyer")
                    startActivity(intent)
                }

                R.id.logOut -> {
                    showDialogFun()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun showDialogFun() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Do you want to sign out?")
            .setCancelable(true)
            .setPositiveButton("Proceed") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                BuyerActivity.currentUser = null
                activity?.finish()
                Toast.makeText(requireContext(), "Signed out", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        val alert = dialogBuilder.create()
        alert.setTitle("Sign Out")
        alert.show()
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

    private fun checkUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
            // Permission Permission is granted
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

    override fun onResume() {
        super.onResume()
        (activity as BuyerActivity?)?.supportActionBar?.hide()
        bottomNavigationBuyer.menu.findItem(R.id.homePage).isChecked = true
        checkUserActiveRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
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
}



