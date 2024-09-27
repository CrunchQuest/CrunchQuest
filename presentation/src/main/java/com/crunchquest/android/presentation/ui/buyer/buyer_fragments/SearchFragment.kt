@file:Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")

package com.crunchquest.android.presentation.ui.buyer.buyer_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.crunchquest.shared.R
import com.crunchquest.android.domain.entities.Service
import com.crunchquest.android.presentation.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    lateinit var v: View
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<Service>
    var arrayList: ArrayList<Service> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search, container, false)

        // Handle back press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Check if the current fragment is SearchFragment
                val currentFragment = parentFragmentManager.findFragmentById(R.id.wrapper)
                if (currentFragment is SearchFragment) {
                    // Replace with HomeFragment
                    parentFragmentManager.beginTransaction().replace(R.id.wrapper, HomeFragment()).commit()
                } else {
                    // If not, perform the default back button action
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        //Map the Search View
        searchView = v.findViewById(R.id.searchView)
        listView = v.findViewById(R.id.listView_fragmentSearch)
        //automatically opens the search view. As soon as this fragment is used, the search view is already clicked.
        searchView.isIconified = false


        // Set an OnClickListener to request focus when the SearchView is clicked
        searchView.setOnClickListener {
            searchView.requestFocus()
            adapter.filter.filter(searchView.query)
        }

        //Implement search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (arrayList.any { service -> service.serviceName == query }) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(v.context, "Not found", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    searchView.requestFocus()
                }
                adapter.filter.filter(newText)
                return false
            }
        })
        listView.setOnItemClickListener { _, _, position, _ ->


        }


        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Fetch services from firebase database
        fetchServices()
    }

    private fun fetchServices() {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("/services")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                snapshot.children.forEach { user ->
                    if (user.key != currentUser) {
                        user.children.forEach { serviceListed ->
                            val service = serviceListed.getValue(Service::class.java)!!
                            if (service.status == "ACTIVE") {
                                arrayList.add(service)
                            }
                        }

                    }
                }
                adapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1, arrayList)
                listView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    //Whenever the fragment is changed to fragment_search, the action bar will disappear
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

    }

    //Whenever the fragment is not fragment_search, the action bar will appear
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }


}