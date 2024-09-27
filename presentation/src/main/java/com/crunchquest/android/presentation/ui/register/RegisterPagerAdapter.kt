package com.crunchquest.android.presentation.ui.register

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class RegisterPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegisterStepOneFragment()
            1 -> RegisterStepTwoFragment()
            2 -> RegisterStepThreeFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
