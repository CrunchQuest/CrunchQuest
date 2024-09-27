package com.crunchquest.android.presentation.ui.components.groupie_views

import android.widget.ImageView
import android.widget.TextView
import com.crunchquest.android.domain.entities.Message
import com.crunchquest.shared.R
import com.crunchquest.android.domain.entities.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class LatestMessageRow(val message: Message) : Item<ViewHolder>() {

    var chatPartnerUser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val chartPartnerId: String

        val ref = FirebaseDatabase.getInstance().getReference("/users/${message.senderId}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.findViewById<TextView>(R.id.usernameTextView_messagesRow).text = "${chatPartnerUser!!.firstName} ${chatPartnerUser!!.lastName}"
                val targetImageView = viewHolder.itemView.findViewById<ImageView>(R.id.imageView_messagesrow)
                Picasso.get().load(chatPartnerUser!!.profilePicture).into(targetImageView)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        viewHolder.itemView.findViewById<TextView>(R.id.messageTextView_messagesRow).text = message.content


    }

    override fun getLayout(): Int {
        return R.layout.row_messages
    }

}