package com.crunchquest.android.presentation.ui.components.groupie_views

import android.widget.ImageView
import android.widget.TextView
import com.crunchquest.shared.R
import com.crunchquest.android.domain.entities.Service
import com.crunchquest.android.domain.entities.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

//Class used for the Groupie adapter. The layout file service_row.xml is used.
class ServiceItem(val service: Service) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.title_serviceRow).text = service.serviceName!!.toUpperCase()
        viewHolder.itemView.findViewById<TextView>(R.id.descriptionTextView_serviceRow).text = service.serviceDescription
        viewHolder.itemView.findViewById<TextView>(R.id.priceTextView_serviceRow).text = "Rp ${service.price.toString()}"


        val ref = FirebaseDatabase.getInstance().getReference("users/${service.serviceId}")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(User::class.java)
                Picasso.get().load(userData!!.profilePicture).into(viewHolder.itemView.findViewById<ImageView>(
                    R.id.imageView_serviceRow))
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun getLayout(): Int {
        return R.layout.row_service
    }

}
