@file:Suppress("DEPRECATION")

package com.crunchquest.android.presentation.ui.serviceprovider.seller_fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.crunchquest.shared.R
import com.crunchquest.android.presentation.ui.components.groupie_views.ViewPagerAdapter
import com.crunchquest.android.presentation.ui.serviceprovider.SellerActivity
import com.crunchquest.android.presentation.ui.serviceprovider.bottomNavigationSeller
import com.crunchquest.android.presentation.ui.serviceprovider.sellermanage_order_fragments.SellerOnGoingFragment
import com.crunchquest.android.presentation.ui.serviceprovider.sellermanage_order_fragments.SellerOrdersFragment
import com.google.android.material.tabs.TabLayout


class SellerManageFragment : Fragment() {


    private lateinit var tabLayout: TabLayout
    private lateinit var v: View
    private lateinit var viewPager: ViewPager
    private var finishedFragment =
        com.crunchquest.android.presentation.ui.serviceprovider.sellermanage_order_fragments.SellerFinishedFragment()
    private var onGoingFragment = SellerOnGoingFragment()
    private var ordersFragment = SellerOrdersFragment()
    private var myContext: FragmentActivity? = null
    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_seller_manage, container, false)
        tabLayout = v.findViewById(R.id.tabLayout_fragmentSellerManage)
        viewPager = v.findViewById(R.id.viewPager_fragmentSellerManage)


        tabLayout.setupWithViewPager(viewPager)

        val fragManager = myContext!!.supportFragmentManager

        val viewPagerAdapter: ViewPagerAdapter = ViewPagerAdapter(fragManager, 0)
        viewPagerAdapter.addFragment(ordersFragment, "ORDERS")
        viewPagerAdapter.addFragment(onGoingFragment, "ACCEPTED")
        viewPagerAdapter.addFragment(finishedFragment, "FINISHED")
        viewPager.adapter = viewPagerAdapter

        return v

    }

    companion object;


    override fun onResume() {
        super.onResume()
        (activity as SellerActivity?)?.setActionBarTitle("Manage Bookings")
        bottomNavigationSeller.menu.findItem(R.id.Seller_manageOrdersPage).isChecked = true
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onDestroy() {
        super.onDestroy()
        fragmentManager!!.beginTransaction().remove(ordersFragment).commitAllowingStateLoss()
        fragmentManager!!.beginTransaction().remove(onGoingFragment).commitAllowingStateLoss()
        fragmentManager!!.beginTransaction().remove(finishedFragment).commitAllowingStateLoss()

    }
}



