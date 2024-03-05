package com.example.crunchquest.ui.components.groupie_views

import android.widget.TextView
import com.example.crunchquest.R
import com.example.crunchquest.data.model.ServiceRequest
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class ManageRequestItem(val request: ServiceRequest) : Item<ViewHolder>() {


    override fun bind(viewHolder: ViewHolder, position: Int) {

        val category =  viewHolder.itemView.findViewById<TextView>(R.id.category_resuest)
        val title =  viewHolder.itemView.findViewById<TextView>(R.id.title_request)
        val price = viewHolder.itemView.findViewById<TextView>(R.id.price_request)

        category.text = request.category
        title.text = request.title!!.toUpperCase()
        price.text = "Rp ${request.price}"


    }

    override fun getLayout(): Int {
        return R.layout.row_manage_request
    }
}