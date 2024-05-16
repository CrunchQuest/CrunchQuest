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

class OrdersFragment : Fragment() {
    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    val adapter = GroupAdapter<ViewHolder>().apply {
        spanCount = 2
    }
    private val i = adapter.spanSizeLookup


    companion object {
        const val TAG = "JustSomeRandomTag"
        var orderClicked: Order? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_manage_orders, container, false)
        recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView_manage)
        recyclerView.apply {
            layoutManager = GridLayoutManager(v.context, 2).apply {
                spanSizeLookup = i
            }
        }
        fetchOrders()
        return v
    }

    override fun onResume() {
        super.onResume()
        fetchOrders()

    }


    private fun fetchOrders() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        // Fetch orders where the current user is the one who created the request (booked_by)
        val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$currentUserUid")
        bookedByRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.children.any()) {
                    // If there are orders where the current user is the one who created the request, fetch them
                    fetchOrdersFromDatabase(bookedByRef, "Request User")
                } else {
                    // If there are no orders where the current user is the one who created the request, fetch orders where the current user is the one who assists the request (booked_to)
                    val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/$currentUserUid")
                    fetchOrdersFromDatabase(bookedToRef, "Assist User")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun fetchOrdersFromDatabase(ref: DatabaseReference, userType: String) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) { // Check if the Fragment is currently added to its activity
                    adapter.clear()
                    Log.d("OrdersFragment", "$userType DataSnapshot: $snapshot") // Log the DataSnapshot
                    snapshot.children.forEach { orderSnapshot ->
                        val order = orderSnapshot.getValue(Order::class.java) ?: return
                        if (order.status == "NEW") {
                            val orderItem = OrderItem(order, requireContext())
                            adapter.add(orderItem)
                            Log.d("OrdersFragment", "$userType Added OrderItem: $orderItem") // Log the OrderItem
                        }
                        // Retrieve tempUser from the Order object
                        val tempUser = order.assistUser
                        Log.d("OrdersFragment", "$userType tempUser: $tempUser") // Log tempUser
                    }
                    // Set item click listener outside the loop
                    adapter.setOnItemClickListener { item, _ ->
                        val orderItem = item as OrderItem
                        val tappedOrder = orderItem.order
                        orderClicked = tappedOrder

                        // Log the Order Object
                        Log.d("OrdersFragment", "$userType Tapped Order: $tappedOrder")

                        if (tappedOrder.assistUser == null) {
                            // If the order has not been assisted, navigate to BottomFragmentOrderDetails
                            val orderDetailsFragment = BottomFragmentOrderDetails(orderClicked!!)
                            orderDetailsFragment.show(parentFragmentManager, TAG)
                        } else {
                            // If the order has been assisted, navigate to BottomFragmentRequestDetails
                            val bundle = Bundle()
                            bundle.putString("requestUser", tappedOrder.assistUser)
                            val requestDetailsFragment = BottomFragmentRequestDetails(orderClicked!!)
                            requestDetailsFragment.arguments = bundle
                            requestDetailsFragment.show(parentFragmentManager, TAG)
                        }
                    }
                    recyclerView.adapter = adapter
                    Log.d("OrdersFragment", "$userType Adapter Item Count: ${adapter.itemCount}") // Log the adapter item count
                } else {
                    Log.d("OrdersFragment", "$userType Has no orders") // Log that there are no orders for the current user
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


}