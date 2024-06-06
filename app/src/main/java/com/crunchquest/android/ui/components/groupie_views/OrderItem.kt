@file:Suppress("DEPRECATION")

package com.crunchquest.android.ui.components.groupie_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.crunchquest.android.R
import com.crunchquest.android.data.model.Order
import com.crunchquest.android.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import java.text.NumberFormat
import java.util.Locale

class OrderItem(val order: Order, val c: Context) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val status = viewHolder.itemView.findViewById<TextView>(R.id.statusTextView_rowBookings)!!
        val date = viewHolder.itemView.findViewById<TextView>(R.id.date_rowBookings)!!
        val title = viewHolder.itemView.findViewById<TextView>(R.id.title_rowBookings)!!
        val category = viewHolder.itemView.findViewById<TextView>(R.id.category_rowBookings)!!
        val time = viewHolder.itemView.findViewById<TextView>(R.id.time_rowBookings)!!
        val description = viewHolder.itemView.findViewById<TextView>(R.id.tv_description_rowBookings)!!
        val person = viewHolder.itemView.findViewById<TextView>(R.id.tv_person)!!
        val price = viewHolder.itemView.findViewById<TextView>(R.id.tv_price)!!
        val layout = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.layout_rowBookings)

        when (order.status) {
            "NEW" -> {
                val background: Drawable? = ContextCompat.getDrawable(c, R.drawable.status_background_new)
                status.background = background
                status.setTextColor(ContextCompat.getColor(c, R.color.dark_washed_blue)) // Set desired text color for NEW status
            }
            "ACCEPTED" -> {
                val background: Drawable? = ContextCompat.getDrawable(c, R.drawable.status_background_ongoing)
                status.background = background
                status.setTextColor(ContextCompat.getColor(c, R.color.dark_orange)) // Set desired text color for ON_GOING status
            }
            "COMPLETE" -> {
                val background: Drawable? = ContextCompat.getDrawable(c, R.drawable.status_background_finished)
                status.background = background
                status.setTextColor(ContextCompat.getColor(c, R.color.dark_green)) // Set desired text color for FINISHED status
            }
        }

        status.text = order.status
        date.text = order.date
        title.text = order.title
        category.text = order.category
        time.text = order.time
        description.text = order.description

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val createdByRef = FirebaseDatabase.getInstance().getReference("/users/${order.bookedBy}")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                val userDisplayName = "${user?.firstName} ${user?.lastName}"
                Log.d("UserDisplayName", userDisplayName)
                order.bookedTo?.let { Log.d("BookedTo", it) }
                if (order.bookedTo == "") {
                    person.text = "You"
                    person.setTextColor(ContextCompat.getColor(c, R.color.dark_cq_purple))
                    layout.setBackgroundResource(R.drawable.order_current_user)
                }

                else if (order.name == userDisplayName) {
                    createdByRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val createdBy = snapshot.getValue(User::class.java)
                            person.text = "${createdBy?.firstName} ${createdBy?.lastName} "
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                    person.setTextColor(ContextCompat.getColor(c, R.color.dark_cq_purple))
                    layout.setBackgroundResource(R.drawable.order_current_user)
                }
                else {
                    person.text = order.name
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(order.price)
        price.text = "Rp$formattedPrice"


    }

    override fun getLayout(): Int {
        return R.layout.row_bookings
    }

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}