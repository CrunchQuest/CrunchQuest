package com.crunchquest.android.ui.components.groupie_views

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.crunchquest.android.R
import com.crunchquest.android.data.model.ServiceRequest
import com.crunchquest.android.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import java.text.NumberFormat
import java.util.Locale


class ServiceRequestItem(val serviceRequest: ServiceRequest, val distance: Double, val similarity: Int, val c: Context) : Item<ViewHolder>() {
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

// Assume you have a reference to your TextView
        val locationTextView = viewHolder.itemView.findViewById<TextView>(R.id.location_tv_serviceRowRequest)

// Assuming 'distance' is the distance value you want to check
// Change these values according to your distance threshold
        val nearDistanceThreshold = 5 // Adjust this threshold as needed
        val mediumDistanceThreshold = 10 // Adjust this threshold as needed

// Assuming 'distance' is the distance value you want to check
        if (distance < nearDistanceThreshold) {
            // Change text color and drawable tint to green for "Very Near"
            locationTextView.setTextColor(ContextCompat.getColor(c, R.color.cool_green))
//            locationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_green, 0, 0, 0)
            locationTextView.text = "Very Near"
        } else if (distance < mediumDistanceThreshold) {
            // Change text color and drawable tint to yellow for "Near"
            locationTextView.setTextColor(ContextCompat.getColor(c, R.color.cool_orange))
//            locationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_yellow, 0, 0, 0)
            locationTextView.text = "Near"
        } else {
            // Change text color and drawable tint to red for "Far"
            locationTextView.setTextColor(ContextCompat.getColor(c, R.color.cool_red))
//            locationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_red, 0, 0, 0)
            locationTextView.text = "Far"
        }

        if (similarity > 6) {
            viewHolder.itemView.findViewById<TextView>(R.id.similarity_tv_serviceRowRequest).text = "Compatible"
        } else {
//            viewHolder.itemView.findViewById<TextView>(R.id.similarity_tv_serviceRowRequest).text = "No similarity"
            viewHolder.itemView.findViewById<TextView>(R.id.similarity_tv_serviceRowRequest).visibility = View.GONE
        }
    }


    override fun getLayout(): Int {
        return R.layout.row_service_requests
    }

}