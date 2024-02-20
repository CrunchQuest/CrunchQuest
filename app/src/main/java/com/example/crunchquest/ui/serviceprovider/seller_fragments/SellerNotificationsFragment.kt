package com.example.crunchquest.ui.serviceprovider.seller_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.crunchquest.R
import com.example.crunchquest.ui.serviceprovider.SellerActivity
import com.example.crunchquest.ui.serviceprovider.bottomNavigationSeller


class SellerNotificationsFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_notifications, container, false)
    }

    companion object;

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as SellerActivity?)?.setActionBarTitle("Notifications")
        bottomNavigationSeller.menu.findItem(R.id.Seller_notificationsPage).isChecked = true
    }
}