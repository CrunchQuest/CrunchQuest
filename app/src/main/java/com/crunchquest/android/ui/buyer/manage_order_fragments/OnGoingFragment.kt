@file:Suppress("DEPRECATION")

package com.crunchquest.android.ui.buyer.manage_order_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.android.R
import com.crunchquest.android.data.model.Order
import com.crunchquest.android.ui.buyer.buyer_fragments.HomeFragment.CustomItemDecoration
import com.crunchquest.android.ui.components.groupie_views.OrderItem
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
        setupAssistRv()
        setupRequestRv()

        fetchOrdersRequest()
        fetchOrdersAssist()
        return v
    }

    private fun setupAssistRv() {
        recyclerViewAssist = v.findViewById(R.id.recyclerView_manage_assist)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewAssist.layoutManager = layoutManager
        recyclerViewAssist.adapter = adapterAssist
        recyclerViewAssist.addItemDecoration(CustomItemDecoration(8))
    }

    private fun setupRequestRv() {
        recyclerViewRequest = v.findViewById(R.id.recyclerView_manage_request)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewRequest.layoutManager = layoutManager
        recyclerViewRequest.adapter = adapterRequest
        recyclerViewRequest.addItemDecoration(CustomItemDecoration(8))
    }

    override fun onResume() {
        super.onResume()
        fetchOrdersRequest()
        fetchOrdersAssist()

    }

    private fun fetchOrdersRequest() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        // Fetch orders where the current user is listed as an requester
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
                        fetchOrdersFromDatabase(serviceOrdersRef, "Requester", recyclerViewRequest, adapterRequest)
                    }
                } else {
                    Log.d("Fetch Request", "No Fetch Request User In Fetch Request Function ")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Fetch Request", "Database error: $error")
            }
        })
    }

    private fun fetchOrdersAssist() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$currentUserUid")
        bookedToRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.children.any()) {
                    fetchOrdersFromDatabase(bookedToRef, "Assist User", recyclerViewAssist, adapterAssist)
                } else {
                    Log.d("Fetch Assist", "No Fetch Request User In Fetch Assist Function ")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Fetch Assist", "Database error: $error")
            }
        })
    }

    private fun fetchOrdersFromDatabase(ref: DatabaseReference, userType: String, recyclerView: RecyclerView, adapter: GroupAdapter<ViewHolder>) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    adapter.clear()
                    Log.d("OnGoingFragment", "$userType DataSnapshot: $snapshot") // Log the DataSnapshot
                    val orders = mutableListOf<OrderItem>()
                    snapshot.children.forEach { orderSnapshot ->
                        val order = orderSnapshot.getValue(Order::class.java) ?: return
                        if (order.status == "ACCEPTED") {
                            val orderItem = OrderItem(order, requireContext())
                            orders.add(orderItem)
                            Log.d("OnGoingFragment", "$userType Added OrderItem: $orderItem") // Log the OrderItem
                        }
                    }

                    adapter.update(orders)

                    adapter.setOnItemClickListener { item, _ ->
                        val orderItem = item as OrderItem
                        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                        val tappedOrder = orderItem.order
                        orderClicked = tappedOrder

                        if (currentUserId == tappedOrder.bookedBy) {
                            if (tappedOrder.assistConfirmation == "TRUE") {
                                val bundle = Bundle()
                                bundle.putString("requestUser", tappedOrder.assistUser)
                                val requestDetailsFragment = BottomFragmentRequestDetails(orderClicked!!) // ACCEPT DECLINE
                                requestDetailsFragment.arguments = bundle
                                requestDetailsFragment.show(parentFragmentManager, TAG)
                            } else {
                                Log.d("OnGoingFragment", "REQUESTER NOT ASSISTED")
                                val orderDetailsFragment = BottomFragmentOrderDetails(orderClicked!!) // CANCEL AND MESSAGE
                                orderDetailsFragment.show(parentFragmentManager, TAG
                                )
                            }
                        } else {
                            // ASSISTER
                            Log.d("OnGoingFragment", "ELSE ASSISTER ONLY")
                            val orderDetailsFragment = BottomFragmentOrderDetails(orderClicked!!) // CANCEL AND MESSAGE
                            orderDetailsFragment.show(parentFragmentManager, TAG)
                        }
                    }

                    recyclerView.adapter = adapter
                    Log.d("OnGoingFragment", "$userType Adapter Item Count: ${adapter.itemCount}")
                } else {
                    Log.d("OnGoingFragment", "$userType Has no orders")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OnGoingFragment", "Database error: $error")
            }
        })
    }
}