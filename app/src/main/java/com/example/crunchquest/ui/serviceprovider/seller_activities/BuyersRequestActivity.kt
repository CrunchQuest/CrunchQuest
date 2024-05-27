package com.example.crunchquest.ui.serviceprovider.seller_activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.crunchquest.R
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.ui.buyer.buyer_activities.DisplaySpecificRequestActivity
import com.example.crunchquest.ui.components.groupie_views.ServiceRequestItem
import com.example.crunchquest.ui.messages.MessagesRequestForSP
import com.example.crunchquest.utility.handlers.ServiceRequestHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class BuyersRequestActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var serviceRequestRecyclerView: RecyclerView
    private lateinit var serviceRequestArrayList: ArrayList<ServiceRequest>
    var currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    private var serviceRequestHandler = ServiceRequestHandler()
    private lateinit var hideThisTextView: TextView
    private lateinit var hideThisImageView: ImageView
    var adapter = GroupAdapter<ViewHolder>()
    private lateinit var messagesBtn: ImageButton

    companion object {
        var serviceRequestToBeViewed: ServiceRequest? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyers_request)
        //Map and initilize everything here
        messagesBtn = findViewById(R.id.imageButton_requestsss)
        toolbar = findViewById(R.id.toolBar_activityBuyersRequest)
        serviceRequestRecyclerView = findViewById(R.id.serviceRequestRecyclerView_activityBuyersRequest)
        serviceRequestArrayList = ArrayList()
        hideThisTextView = findViewById(R.id.hideThisTextView_activityBuyersRequest)
        hideThisImageView = findViewById(R.id.hideThisImageView_activityBuyersRequest)
        serviceRequestRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        toolbar.title = "Client Request"
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        //Gets all the Service Requests available
        val adapter = GroupAdapter<ViewHolder>()
        serviceRequestRecyclerView.adapter = adapter
        fetchServiceRequests()

        messagesBtn.setOnClickListener {
            //Go to different activity
            startActivity(Intent(this, MessagesRequestForSP::class.java))
        }
    }

    private fun fetchServiceRequests() {
        serviceRequestHandler.serviceRequestRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                adapter.clear()
                p0.children.forEach {
                    val serviceRequest = it.getValue(ServiceRequest::class.java)
                    if (serviceRequest!!.userUid != currentUserUid) {
//                        adapter.add(ServiceRequestItem(serviceRequest))
                    }
                }
                serviceRequestRecyclerView.adapter = adapter

                adapter.setOnItemClickListener { item, view ->
                    val serviceRequestItem = item as ServiceRequestItem
                    val intent = Intent(this@BuyersRequestActivity, DisplaySpecificRequestActivity::class.java)
                    intent.putExtra("ServiceRequest", serviceRequestItem.serviceRequest)
                    startActivity(intent)


                }

//                hideOrShowViews()
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("TAG", "Failed to fetch Service Requests" , p0.toException())

            }
        })
    }

    private fun hideOrShowViews() {
        if (adapter.itemCount == 0) {
            hideThisTextView.isVisible = true
            hideThisImageView.isVisible = true
        } else {
            hideThisTextView.isVisible = false
            hideThisImageView.isVisible = false

        }
    }

}



