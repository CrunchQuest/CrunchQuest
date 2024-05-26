package com.example.crunchquest.ui.components.groupie_views

import android.widget.ImageView
import android.widget.TextView
import com.example.crunchquest.R
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import java.text.NumberFormat
import java.util.Locale


class ServiceRequestItem(val serviceRequest: ServiceRequest) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.title_serviceRowRequest).text = serviceRequest.title!!.toUpperCase()
        viewHolder.itemView.findViewById<TextView>(R.id.descriptionTextView_serviceRowRequest).text = serviceRequest.description
        val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(serviceRequest.price)
        viewHolder.itemView.findViewById<TextView>(R.id.priceTextView_serviceRowRequest).text = "Rp$formattedPrice"

        if (serviceRequest.category == "Computer Repair") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_computer)
        } else if (serviceRequest.category == "Home Cleaning") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_homecleaning)
        } else if (serviceRequest.category == "Plumbing") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_plumbing)
        } else if (serviceRequest.category == "Electrical") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_electrical)
        } else if (serviceRequest.category == "Moving") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_moving)
        } else if (serviceRequest.category == "Delivery") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_delivery)
        } else if (serviceRequest.category == "AC Repair") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_aircon)
        } else if (serviceRequest.category == "Home Repair") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_homerepair)
        } else if (serviceRequest.category == "Auto Repair") {
            viewHolder.itemView.findViewById<ImageView>(R.id.categoryImageView_serviceRowRequests).setImageResource(R.drawable.services_auto)
        }
//        viewHolder.itemView.findViewById<TextView>(R.id.categoryTextView_serviceRowRequests).text = serviceRequest.category


        val ref = FirebaseDatabase.getInstance().getReference("users/${serviceRequest.userUid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(User::class.java)
                Picasso.get().load(userData!!.profileImageUrl).into(viewHolder.itemView.findViewById<ImageView>(
                    R.id.imageView_serviceRowRequest))
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun getLayout(): Int {
        return R.layout.row_service_requests
    }

}