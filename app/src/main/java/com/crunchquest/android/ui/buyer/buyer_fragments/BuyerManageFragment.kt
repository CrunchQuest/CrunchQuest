@file:Suppress("DEPRECATION")

package com.crunchquest.android.ui.buyer.buyer_fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.crunchquest.android.R
import com.crunchquest.android.ui.buyer.BuyerActivity
import com.crunchquest.android.ui.buyer.bottomNavigationBuyer
import com.crunchquest.android.ui.buyer.manage_order_fragments.FinishedFragment
import com.crunchquest.android.ui.buyer.manage_order_fragments.OnGoingFragment
import com.crunchquest.android.ui.buyer.manage_order_fragments.OrdersFragment
import com.crunchquest.android.ui.components.groupie_views.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class BuyerManageFragment : Fragment() {
    lateinit var v: View

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private var finishedFragment = FinishedFragment()
    private var onGoingFragment = OnGoingFragment()
    private var ordersFragment = OrdersFragment()
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
        v = inflater.inflate(R.layout.fragment_buyer_manage, container, false)
        tabLayout = v.findViewById(R.id.tabLayout_fragmentBuyerManage)
        viewPager = v.findViewById(R.id.viewPager_fragmentBuyerManage)
        tabLayout.setupWithViewPager(viewPager)

        val fragManager = myContext!!.supportFragmentManager
        val viewPagerAdapter: ViewPagerAdapter = ViewPagerAdapter(fragManager, 0)
        viewPagerAdapter.addFragment(ordersFragment, "ORDERS")
        viewPagerAdapter.addFragment(onGoingFragment, "ON GOING")
        viewPagerAdapter.addFragment(finishedFragment, "FINISHED")
        viewPager.adapter = viewPagerAdapter
        return v
    }

    companion object;

    override fun onResume() {
        super.onResume()
        (activity as BuyerActivity?)?.setActionBarTitle("Manage Requests")
        bottomNavigationBuyer.menu.findItem(R.id.notifications).isChecked = true
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onDestroy() {
        super.onDestroy()
        fragmentManager!!.beginTransaction().remove(ordersFragment).commitAllowingStateLoss()
        fragmentManager!!.beginTransaction().remove(onGoingFragment).commitAllowingStateLoss()
        fragmentManager!!.beginTransaction().remove(finishedFragment).commitAllowingStateLoss()

    }
}