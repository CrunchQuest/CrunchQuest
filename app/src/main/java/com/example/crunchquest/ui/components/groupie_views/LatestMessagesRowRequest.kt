package com.example.crunchquest.ui.components.groupie_views

import android.widget.ImageView
import android.widget.TextView
import com.example.crunchquest.R
import com.example.crunchquest.data.model.RequestMessage
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class LatestMessagesRowRequest(val message: RequestMessage) : Item<ViewHolder>() {

    var chatPartnerUser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val chartPartnerId: String
        if (message.fromId == FirebaseAuth.getInstance().currentUser!!.uid) {
            chartPartnerId = message.toId!!
        } else {
            chartPartnerId = message.fromId!!
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chartPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                val anotherRef = FirebaseDatabase.getInstance().getReference("service_requests/${message.requestUid}")
                anotherRef.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val requestData = snapshot.getValue(ServiceRequest::class.java)
                        viewHolder.itemView.findViewById<TextView>(R.id.usernameTextView_messagesRow).text = "${requestData!!.title}(${requestData.category})"
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                val targetImageView = viewHolder.itemView.findViewById<ImageView>(R.id.imageView_messagesrow)
                Picasso.get().load(chatPartnerUser!!.profileImageUrl).into(targetImageView)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        viewHolder.itemView.findViewById<TextView>(R.id.messageTextView_messagesRow).text = message.text


    }

    override fun getLayout(): Int {
        return R.layout.row_messages
    }
}