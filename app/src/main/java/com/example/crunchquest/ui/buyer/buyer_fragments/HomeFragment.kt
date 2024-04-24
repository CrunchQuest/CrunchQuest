package com.example.crunchquest.ui.buyer.buyer_fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crunchquest.R
import com.example.crunchquest.ui.buyer.BuyerActivity
import com.example.crunchquest.ui.buyer.bottomNavigationBuyer
import com.example.crunchquest.ui.buyer.buyer_activities.RequestActivity
import com.example.crunchquest.ui.buyer.buyer_activities.ServiceCategoryActivity
import com.example.crunchquest.ui.components.groupie_views.ServiceCategoryItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class HomeFragment : Fragment() {

    private lateinit var customFabLayout: View
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
        //Map everything here
        recyclerView = v.findViewById(R.id.recyclerView_fragmentHome)
        recyclerView.apply {
            layoutManager = GridLayoutManager(v.context, 2).apply {
                spanSizeLookup = i
            }
        }
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
        (activity as BuyerActivity?)?.setActionBarTitle("Services")
        bottomNavigationBuyer.menu.findItem(R.id.homePage).isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove callbacks to prevent memory leaks
        fabHandler.removeCallbacks(longClickRunnable)
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