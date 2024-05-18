@file:Suppress("DEPRECATION")

package com.example.crunchquest.ui.buyer.manage_order_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crunchquest.R
import com.example.crunchquest.data.model.Order
import com.example.crunchquest.ui.components.groupie_views.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class OnGoingFragment : Fragment() {
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
        recyclerViewRequest = v.findViewById<RecyclerView>(R.id.recyclerView_manage_request)
        recyclerViewRequest.apply {
            layoutManager = GridLayoutManager(v.context, 2).apply {
                spanSizeLookup = iRequest
            }
        }

        recyclerViewAssist = v.findViewById<RecyclerView>(R.id.recyclerView_manage_assist)
        recyclerViewAssist.apply {
            layoutManager = GridLayoutManager(v.context, 2).apply {
                spanSizeLookup = iAssist
            }
        }

        fetchOrdersRequest()
        fetchOrdersAssist()
        return v
    }

    override fun onResume() {
        super.onResume()
        fetchOrdersRequest()
        fetchOrdersAssist()

    }

    private fun fetchOrdersRequest() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        // Fetch orders where the current user is the one who created the request (booked_by)
        val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$currentUserUid")
        bookedByRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.children.any()) {
                    // If there are orders where the current user is the one who created the request, fetch them
                    fetchOrdersFromDatabase(bookedByRef, "Request User", recyclerViewRequest, adapterRequest)
                } else {
                    // If there are no orders where the current user is the one who created the request, fetch orders where the current user is the one who assists the request (booked_to)
//                    val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$currentUserUid")
//                    fetchOrdersFromDatabase(bookedToRef, "Assist User")
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
                    Log.d("OnGoingFragment", "$userType DataSnapshot: $snapshot") // Log the DataSnapshot
                    snapshot.children.forEach { orderSnapshot ->
                        val order = orderSnapshot.getValue(Order::class.java) ?: return
                        if (order.status == "ACCEPTED") {
                            val orderItem = OrderItem(order, requireContext())
                            adapter.add(orderItem)
                            Log.d("OnGoingFragment", "$userType Added OrderItem: $orderItem") // Log the OrderItem
                        }
                        // Retrieve tempUser from the Order object
                        val tempUser = order.assistUser
                        Log.d("OnGoingFragment", "$userType tempUser: $tempUser") // Log tempUser
                    }
                    // Set item click listener outside the loop
                    adapter.setOnItemClickListener { item, _ ->
                        val orderItem = item as OrderItem
                        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                        val tappedOrder = orderItem.order
                        FinishedFragment.orderClicked = tappedOrder

                        // Log the Order Object
                        Log.d("OnGoingFragment", "$userType Tapped Order: $tappedOrder")
                        Log.d("OnGoingFragment", "Current User ID: $currentUserId")
                        Log.d("OnGoingFragment", "Booked By: ${tappedOrder.bookedBy}")
                        Log.d("OnGoingFragment", "tappedOrder.userUid: ${tappedOrder.userUid}")
                        if (currentUserId == tappedOrder.bookedBy) {

                            Log.d("OnGoingFragment", "REQUESTER ONLY")
                            // REQUESTER
                            if (tappedOrder.assistConfirmation == "TRUE") {
                                Log.d("OnGoingFragment", "REQUESTER AND ASSISTED")
                                val bundle = Bundle()
                                bundle.putString("requestUser", tappedOrder.assistUser)
                                val requestDetailsFragment = BottomFragmentRequestDetails(
                                    FinishedFragment.orderClicked!!) // ACCEPT DECLINE
                                requestDetailsFragment.arguments = bundle
                                requestDetailsFragment.show(parentFragmentManager,
                                    FinishedFragment.TAG
                                )
                            } else {
                                Log.d("OnGoingFragment", "REQUESTER NOT ASSISTED")
                                val orderDetailsFragment = BottomFragmentOrderDetails(
                                    FinishedFragment.orderClicked!!) // CANCEL AND MESSAGE
                                orderDetailsFragment.show(parentFragmentManager,
                                    FinishedFragment.TAG
                                )
                            }
                        } else {
                            // ASSISTER
                            Log.d("OnGoingFragment", "ELSE ASSISTER ONLY")
                            val orderDetailsFragment = BottomFragmentOrderDetails(FinishedFragment.orderClicked!!) // CANCEL AND MESSAGE
                            orderDetailsFragment.show(parentFragmentManager, FinishedFragment.TAG)
                        }
                    }
                    recyclerView.adapter = adapter
                    Log.d("OnGoingFragment", "$userType Adapter Item Count: ${adapter.itemCount}") // Log the adapter item count
                } else {
                    Log.d("OnGoingFragment", "$userType Has no orders") // Log that there are no orders for the current user
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


}