@file:Suppress("DEPRECATION")

package com.crunchquest.android.presentation.ui.buyer.manage_order_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.shared.R
import com.crunchquest.android.domain.model.Order
import com.crunchquest.android.presentation.ui.components.groupie_views.OrderItem
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

    companion object {
        const val TAG = "JustSomeRandomTag"
        var orderClicked: Order? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_activity, container, false)

        setupRequestRv()
        setupAssistRv()

        fetchOrdersRequest()
        fetchOrdersAssist()
        return v
    }

    private fun setupAssistRv() {
        recyclerViewAssist = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewAssist.layoutManager = layoutManager
        recyclerViewAssist.adapter = adapterAssist
    }

    private fun setupRequestRv() {
        recyclerViewRequest = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewRequest.layoutManager = layoutManager
        recyclerViewRequest.adapter = adapterRequest
    }

    override fun onResume() {
        super.onResume()
        fetchOrdersRequest()
        fetchOrdersAssist()
    }

    private fun fetchOrdersRequest() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$currentUserUid")

        bookedByRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val services = snapshot.children.mapNotNull { it.key }
                    fetchOrdersForServices(services, currentUserUid, recyclerViewRequest, adapterRequest)
                } else {
                    Log.d("Fetch Request", "No Fetch Assist User In Fetch Request Function")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Fetch Request", "Database error: $error")
            }
        })
    }

    private fun fetchOrdersForServices(services: List<String>, userUid: String, recyclerView: RecyclerView, adapter: GroupAdapter<ViewHolder>) {
        val orders = mutableListOf<OrderItem>()
        services.forEach { serviceUid ->
            val serviceOrdersRef = FirebaseDatabase.getInstance().getReference("booked_by/$userUid/$serviceUid")
            serviceOrdersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { orderSnapshot ->
                            val order = orderSnapshot.getValue(Order::class.java) ?: return
                            if (order.status == "COMPLETED") {
                                val orderItem = OrderItem(order, requireContext())
                                orders.add(orderItem)
                            }
                        }
                        adapter.update(orders)
                        setItemClickListener(adapter)
                        recyclerView.adapter = adapter
                    } else {
                        Log.d("Fetch Request", "No orders for service $serviceUid")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Fetch Request", "Database error: $error")
                }
            })
        }
    }

    private fun fetchOrdersAssist() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$currentUserUid")

        bookedToRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.children.any()) {
                    fetchOrdersFromDatabase(bookedToRef, "Assist User", recyclerViewAssist, adapterAssist)
                } else {
                    Log.d("Fetch Assist", "No Fetch Request User In Fetch Assist Function")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Fetch Assist", "Database error: $error")
            }
        })
    }

    private fun fetchOrdersFromDatabase(ref: DatabaseReference, userType: String, recyclerView: RecyclerView, adapter: GroupAdapter<ViewHolder>) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    val orders = mutableListOf<OrderItem>()
                    snapshot.children.forEach { orderSnapshot ->
                        val order = orderSnapshot.getValue(Order::class.java) ?: return
                        if (order.status == "COMPLETED") {
                            val orderItem = OrderItem(order, requireContext())
                            orders.add(orderItem)
                        }
                    }

                    if (orders.isNotEmpty()) {
                        adapter.update(orders)
                        setItemClickListener(adapter)
                        recyclerView.adapter = adapter
                    } else {
                        Log.d("FinishedFragment", "No completed orders found for $userType")
                    }
                } else {
                    Log.d("FinishedFragment", "$userType Has no orders")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FinishedFragment", "Database error: $error")
            }
        })
    }

    private fun setItemClickListener(adapter: GroupAdapter<ViewHolder>) {
        adapter.setOnItemClickListener { item, _ ->
            val orderItem = item as OrderItem
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val tappedOrder = orderItem.order
            orderClicked = tappedOrder

            if (currentUserId == tappedOrder.bookedBy) {
                if (tappedOrder.assistConfirmation == "TRUE") {
                    val bundle = Bundle()
                    bundle.putString("requestUser", tappedOrder.assistUser)
                    val requestDetailsFragment = BottomFragmentRequestDetails(orderClicked!!)
                    requestDetailsFragment.arguments = bundle
                    requestDetailsFragment.show(parentFragmentManager, TAG)
                } else {
                    val orderDetailsFragment = BottomFragmentOrderDetails(orderClicked!!)
                    orderDetailsFragment.show(parentFragmentManager, TAG)
                }
            } else {
                val orderDetailsFragment = BottomFragmentOrderDetails(orderClicked!!)
                orderDetailsFragment.show(parentFragmentManager, TAG)
            }
        }
    }
}

