package com.crunchquest.android.ui.components.groupie_views

import android.widget.ImageView
import android.widget.TextView
import com.crunchquest.android.R
import com.crunchquest.android.data.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFromItem(val text: String, val user: User, val timeStamp: Long) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textView_chatFromRow).text = text
        viewHolder.itemView.findViewById<TextView>(R.id.timeStamp_chatFromRow).text = convertLongToDate(timeStamp)
        //load user image
        val targetImageView = viewHolder.itemView.findViewById<ImageView>(R.id.imageView_chatFromRow)
        Picasso.get().load(user.profileImageUrl).into(targetImageView)


    }

    override fun getLayout(): Int {
        return R.layout.row_chat_from
    }

    fun convertLongToDate(long: Long): String {
        val date = Date(long)
        val format = SimpleDateFormat("MMM d HH:mm", Locale.getDefault())
        return format.format(date)
    }
}