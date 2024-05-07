package com.example.crunchquest.ui.buyer.buyer_fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.crunchquest.R
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.ui.adapter.SliderAdapter
import com.example.crunchquest.ui.buyer.BuyerActivity
import com.example.crunchquest.ui.buyer.bottomNavigationBuyer
import com.example.crunchquest.ui.buyer.buyer_activities.DisplaySpecificRequestActivity
import com.example.crunchquest.ui.buyer.buyer_activities.RequestActivity
import com.example.crunchquest.ui.buyer.buyer_activities.ServiceCategoryActivity
import com.example.crunchquest.ui.components.groupie_views.ServiceCategoryItem
import com.example.crunchquest.ui.components.groupie_views.ServiceRequestItem
import com.example.crunchquest.ui.general.LoginActivity
import com.example.crunchquest.ui.general.ProfileSettingsActivity
import com.example.crunchquest.utility.handlers.ServiceRequestHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class HomeFragment : Fragment() {

    private lateinit var mainFab: FloatingActionButton
    private lateinit var subFab1: FloatingActionButton
    private lateinit var subFab2: FloatingActionButton
    private lateinit var subFab3: FloatingActionButton
    private lateinit var fabHandler: Handler
    private lateinit var longClickRunnable: Runnable
    private var isExpanded = false

    // Maps
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    // Service Request
    lateinit var serviceRequestRecyclerView: RecyclerView
    private lateinit var serviceRequestArrayList: ArrayList<ServiceRequest>
    private var serviceRequestHandler = ServiceRequestHandler()
    var currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid


    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    val adapterCategory = GroupAdapter<ViewHolder>().apply {
        spanCount = 2
    }
    private val i = adapterCategory.spanSizeLookup

    val adapterRequest = GroupAdapter<ViewHolder>().apply {
        spanCount = 1
    }


    val searchFragment = SearchFragment()

    companion object {
        val SERVICECATEGORY = "serviceCategory"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar: Toolbar = v.findViewById(R.id.my_toolbar)
        (activity as AppCompatActivity).supportActionBar?.hide()

        val circleImageView: ImageView = toolbar.findViewById(R.id.circleImageView)

        circleImageView.setOnClickListener {
            // Handle click event here
            val popupMenu = PopupMenu(v.context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)

            // Remove unnecessary items
            popupMenu.menu.removeItem(R.id.addService)
            popupMenu.menu.removeItem(R.id.search)
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

        val images = listOf(
            R.drawable.food1,
            R.drawable.food2,
            R.drawable.food3,
            R.drawable.food4,
            R.drawable.food5
        )
        val viewPager = v.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = SliderAdapter(images)

        val tabLayout = v.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        // Get the user ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        // Check if userId is not null
        if (userId != null) {
            // Get the reference to the image in Firebase Storage
            val storageReference =
                FirebaseStorage.getInstance().reference.child("profileImages/${userId}")

            // Load the image into the CircleImageView
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_person)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "Load failed", e)
                            // You can also log the individual causes:
                            for (cause in e!!.rootCauses) {
                                Log.e("TAG", "Caused by", cause)
                            }
                            // Or, to log all individual causes locally, you can use:
                            e.logRootCauses("TAG")
                            return false // Allow calling onLoadFailed on the Target.
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(circleImageView)
            }
                .addOnFailureListener { exception ->
                    // Handle any errors here, such as displaying a Toast or loading a default image
                    Log.e("TAG", "Failed to download image", exception)
                    circleImageView.setImageResource(R.drawable.ic_person)
                }
        } else {
            // If userId is null, load the default image
            circleImageView.setImageResource(R.drawable.ic_person)
        }


        //Map everything here
        recyclerView = v.findViewById(R.id.recyclerView_fragmentHome)
        recyclerView.apply {
            layoutManager = GridLayoutManager(v.context, 5).apply {
                spanSizeLookup = adapterCategory.spanSizeLookup
            }
            adapter = adapterCategory
        }

        recyclerView.addItemDecoration(CustomItemDecoration(-50)) // replace 10 with your desired space

        val btn = v.findViewById<Button>(R.id.searchBtn)
        //Button onclick listener
        btn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.wrapper, searchFragment)
            transaction?.commit()
        }

        // Quest RecycleView
        serviceRequestRecyclerView =
            v.findViewById(R.id.serviceRequestRecyclerView_activityBuyersRequest)
        serviceRequestArrayList = ArrayList()

        //Floating action button
        mainFab = v.findViewById(R.id.mainFab)
        subFab1 = v.findViewById(R.id.subFab1)
        subFab2 = v.findViewById(R.id.subFab2)
        subFab3 = v.findViewById(R.id.subFab3)
        fabHandler = Handler()

        longClickRunnable = Runnable {
            expandSubFabs()
        }

        mainFab.setOnLongClickListener {
            fabHandler.postDelayed(longClickRunnable, 1000)
            true
        }
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
        mainFab.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                // Cancel the long click handler if the button is released before 3 seconds
                fabHandler.removeCallbacks(longClickRunnable)
            }
            false
        }
        subFab1.setOnClickListener {
            Toast.makeText(context, "Example of sub fab 1", Toast.LENGTH_SHORT).show()
        }
        subFab2.setOnClickListener {
            Toast.makeText(context, "Example of sub fab 2", Toast.LENGTH_SHORT).show()
        }
        subFab3.setOnClickListener {
            Toast.makeText(context, "Example of sub fab 3", Toast.LENGTH_SHORT).show()
        }

        fetchServiceCategory()

        //Gets all the Service Requests available
        serviceRequestRecyclerView.adapter = adapterRequest
        fetchServiceRequests()

        return v
    }

    private fun fetchServiceRequests() {
        serviceRequestHandler.serviceRequestRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                adapterRequest.clear()
                p0.children.forEach {
                    val serviceRequest = it.getValue(ServiceRequest::class.java)
                    if (serviceRequest!!.userUid != currentUserUid) {
                        adapterRequest.add(ServiceRequestItem(serviceRequest))
                    }
                }
                serviceRequestRecyclerView.adapter = adapterRequest

                adapterRequest.setOnItemClickListener { item, view ->
                    val serviceRequestItem = item as ServiceRequestItem
                    val intent = Intent(view.context, DisplaySpecificRequestActivity::class.java)
                    intent.putExtra("ServiceRequest", serviceRequestItem.serviceRequest)
                    startActivity(intent)


                }

//                hideOrShowViews()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


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
        adapterCategory.add(ServiceCategoryItem(arrayList[0], R.drawable.services_computer))
        adapterCategory.add(ServiceCategoryItem(arrayList[1], R.drawable.services_homecleaning))
        adapterCategory.add(ServiceCategoryItem(arrayList[2], R.drawable.services_plumbing))
        adapterCategory.add(ServiceCategoryItem(arrayList[3], R.drawable.services_electrical))
        adapterCategory.add(ServiceCategoryItem(arrayList[4], R.drawable.services_moving))
        adapterCategory.add(ServiceCategoryItem(arrayList[5], R.drawable.services_delivery))
        adapterCategory.add(ServiceCategoryItem(arrayList[6], R.drawable.services_aircon))
        adapterCategory.add(ServiceCategoryItem(arrayList[7], R.drawable.services_homerepair))
        adapterCategory.add(ServiceCategoryItem(arrayList[8], R.drawable.services_auto))
        //Attach the adapter to the recycler view
        recyclerView.adapter = adapterCategory

        adapterCategory.setOnItemClickListener { item, _ ->
            val intent = Intent(v.context, ServiceCategoryActivity::class.java)
            val category = item as ServiceCategoryItem
            Log.d("ServiceCategoryTitle", category.serviceTitle)
            intent.putExtra(SERVICECATEGORY, category.serviceTitle)
            startActivity(intent)

        }
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
        fabHandler.removeCallbacks(longClickRunnable)

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
