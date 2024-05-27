@file:Suppress("DEPRECATION")

package com.example.crunchquest.ui.buyer.manage_order_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crunchquest.R
import com.example.crunchquest.data.model.Order
import com.example.crunchquest.ui.buyer.buyer_fragments.CustomItemDecoration
import com.example.crunchquest.ui.components.groupie_views.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class FinishedFragment : Fragment() {
    private lateinit var v: View
    private lateinit var recyclerViewRequest: RecyclerView
    private lateinit var recyclerViewAssist: RecyclerView
    val adapterRequest = GroupAdapter<ViewHolder>().apply {
        spanCount = 2
    }

    val adapterAssist = GroupAdapter<ViewHolder>().apply {
        spanCount = 2
    }
    private val iRequest = adapterRequest.spanSizeLookup
    private val iAssist = adapterAssist.spanSizeLookup


    companion object {
        const val TAG = "JustSomeRandomTag"
        var orderClicked: Order? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_manage_orders, container, false)

        setupRequestRv()
        setupAssistRv()

        fetchOrdersRequest()
        fetchOrdersAssist()
        return v
    }

    private fun setupAssistRv() {
        recyclerViewAssist = v.findViewById(R.id.recyclerView_manage_assist)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewAssist.layoutManager = layoutManager
        recyclerViewAssist.addItemDecoration(CustomItemDecoration(8))
    }

    private fun setupRequestRv() {
        recyclerViewRequest = v.findViewById(R.id.recyclerView_manage_request)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewRequest.layoutManager = layoutManager
        recyclerViewRequest.addItemDecoration(CustomItemDecoration(8))
    }


    override fun onResume() {
        super.onResume()
        fetchOrdersRequest()
        fetchOrdersAssist()

    }

    private fun fetchOrdersRequest() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        // Fetch orders where the current user is listed as an assister
        val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$currentUserUid")
        Log.d("Fetch Assist", "bookedByRef: $bookedByRef")
        bookedByRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Iterate through the list of services where the current user is a requester
                    for (serviceSnapshot in snapshot.children) {
                        val serviceUid = serviceSnapshot.key
                        Log.d("Fetch Assist", "serviceUid: $serviceUid")

                        // Fetch orders for each service where the current user is an requester
                        val serviceOrdersRef = FirebaseDatabase.getInstance().getReference("booked_by/$currentUserUid/$serviceUid")
                        Log.d("Fetch Assist", "serviceOrdersRef: $serviceOrdersRef")
                        fetchOrdersFromDatabase(serviceOrdersRef, "Assister", recyclerViewRequest, adapterRequest)
                    }
                } else {
                    Log.d("Fetch Assist", "No Fetch Assist User In Fetch Request Function ")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun fetchOrdersAssist() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        // Fetch orders where the current user is the one who created the request (booked_by)
        val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$currentUserUid")
        bookedToRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.children.any()) {
                    // If there are orders where the current user is the one who created the request, fetch them
                    fetchOrdersFromDatabase(bookedToRef, "Assist User", recyclerViewAssist, adapterAssist)
                } else {
                    // If there are no orders where the current user is the one who created the request, fetch orders where the current user is the one who assists the request (booked_to)
//                    val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$currentUserUid")
//                    fetchOrdersFromDatabase(bookedToRef, "Request User")
                    Log.d("Fetch Assist", "No Fetch Request User In Fetch Assist Function ")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun fetchOrdersFromDatabase(ref: DatabaseReference, userType: String, recyclerView: RecyclerView, adapter: GroupAdapter<ViewHolder>) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) { // Check if the Fragment is currently added to its activity
                    adapter.clear()
                    Log.d("FinishedFragment", "$userType DataSnapshot: $snapshot") // Log the DataSnapshot
                    snapshot.children.forEach { orderSnapshot ->
                        val order = orderSnapshot.getValue(Order::class.java) ?: return
                        if (order.status == "COMPLETED") {
                            val orderItem = OrderItem(order, requireContext())
                            adapter.add(orderItem)
                            Log.d("FinishedFragment", "$userType Added OrderItem: $orderItem") // Log the OrderItem
                        }
                        // Retrieve tempUser from the Order object
                        val tempUser = order.assistUser
                        Log.d("FinishedFragment", "$userType tempUser: $tempUser") // Log tempUser
                    }
                    // Set item click listener outside the loop
                    adapter.setOnItemClickListener { item, _ ->
                        val orderItem = item as OrderItem
                        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                        val tappedOrder = orderItem.order
                        orderClicked = tappedOrder

                        // Log the Order Object
                        Log.d("FinishedFragment", "$userType Tapped Order: $tappedOrder")
                        Log.d("FinishedFragment", "Current User ID: $currentUserId")
                        Log.d("FinishedFragment", "Booked By: ${tappedOrder.bookedBy}")
                        Log.d("FinishedFragment", "tappedOrder.userUid: ${tappedOrder.userUid}")
                        if (currentUserId == tappedOrder.bookedBy) {

                            Log.d("FinishedFragment", "REQUESTER ONLY")
                            // REQUESTER
                            if (tappedOrder.assistConfirmation == "TRUE") {
                                Log.d("FinishedFragment", "REQUESTER AND ASSISTED")
                                val bundle = Bundle()
                                bundle.putString("requestUser", tappedOrder.assistUser)
                                val requestDetailsFragment = BottomFragmentRequestDetails(orderClicked!!) // ACCEPT DECLINE
                                requestDetailsFragment.arguments = bundle
                                requestDetailsFragment.show(parentFragmentManager, TAG)
                            } else {
                                Log.d("FinishedFragment", "REQUESTER NOT ASSISTED")
                                val orderDetailsFragment = BottomFragmentOrderDetails(orderClicked!!) // CANCEL AND MESSAGE
                                orderDetailsFragment.show(parentFragmentManager, TAG)
                            }
                        } else {
                            // ASSISTER
                            Log.d("FinishedFragment", "ELSE ASSISTER ONLY")
                            val orderDetailsFragment = BottomFragmentOrderDetails(orderClicked!!) // CANCEL AND MESSAGE
                            orderDetailsFragment.show(parentFragmentManager, TAG)
                        }
                    }
                    recyclerView.adapter = adapter
                    Log.d("FinishedFragment", "$userType Adapter Item Count: ${adapter.itemCount}") // Log the adapter item count
                } else {
                    Log.d("FinishedFragment", "$userType Has no orders") // Log that there are no orders for the current user
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


}