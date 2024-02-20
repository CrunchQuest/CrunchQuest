package com.example.crunchquest.ui.components.groupie_views

import android.widget.ImageView
import android.widget.TextView
import com.example.crunchquest.R
import com.xwray.groupie.Group
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder


class ServiceCategoryItem(val serviceTitle: String, val imageResource: Int) : Item<ViewHolder>(),
    Group {


    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.title_serviceCategoryRow).text = serviceTitle
        viewHolder.itemView.findViewById<ImageView>(R.id.imageView_serviceCategoryRow).setImageResource(imageResource)
    }

    override fun getLayout(): Int {
        return R.layout.row_service_category
    }

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}