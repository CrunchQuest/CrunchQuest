package com.example.crunchquest.ui.buyer.buyer_fragments

import android.content.DialogInterface
import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.crunchquest.R
import com.example.crunchquest.ui.buyer.BuyerActivity
import com.example.crunchquest.ui.buyer.bottomNavigationBuyer
import com.example.crunchquest.ui.buyer.buyer_activities.RequestActivity
import com.example.crunchquest.ui.buyer.buyer_activities.ServiceCategoryActivity
import com.example.crunchquest.ui.components.groupie_views.ServiceCategoryItem
import com.example.crunchquest.ui.general.ChooseActivity
import com.example.crunchquest.ui.general.LoginActivity
import com.example.crunchquest.ui.general.ProfileSettingsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
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


    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    val adapter = GroupAdapter<ViewHolder>().apply {
        spanCount = 2
    }
    private val i = adapter.spanSizeLookup


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
                    R.id.changeMode -> {
                        startActivity(Intent(requireContext(), ChooseActivity::class.java))
                    }
                    R.id.logOut -> {
                        showDialogFun()
                    }
                }
                true
            }
            popupMenu.show()
        }

        // Get the user ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        // Check if userId is not null
        if (userId != null) {
            // Get the reference to the image in Firebase Storage
            val storageReference = FirebaseStorage.getInstance().reference.child("profileImages/${userId}")

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
                spanSizeLookup = i
            }
        }

        recyclerView.addItemDecoration(CustomItemDecoration(-50)) // replace 10 with your desired space

        val btn = v.findViewById<Button>(R.id.searchBtn)
        //Button onclick listener
        btn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.wrapper, searchFragment)
            transaction?.commit()
        }

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
                // Navigate to RequestActivity
                val intent = Intent(v.context, RequestActivity::class.java)
                startActivity(intent)
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

        return v
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
        adapter.clear()
        //Get the array of service category
        val arrayList = ArrayList(listOf(*resources.getStringArray(R.array.services_category)))
        //Add the different categories to the adapter
        adapter.add(ServiceCategoryItem(arrayList[0], R.drawable.services_computer))
        adapter.add(ServiceCategoryItem(arrayList[1], R.drawable.services_homecleaning))
        adapter.add(ServiceCategoryItem(arrayList[2], R.drawable.services_plumbing))
        adapter.add(ServiceCategoryItem(arrayList[3], R.drawable.services_electrical))
        adapter.add(ServiceCategoryItem(arrayList[4], R.drawable.services_moving))
        adapter.add(ServiceCategoryItem(arrayList[5], R.drawable.services_delivery))
        adapter.add(ServiceCategoryItem(arrayList[6], R.drawable.services_aircon))
        adapter.add(ServiceCategoryItem(arrayList[7], R.drawable.services_homerepair))
        adapter.add(ServiceCategoryItem(arrayList[8], R.drawable.services_auto))
        //Attach the adapter to the recycler view
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { item, _ ->
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
