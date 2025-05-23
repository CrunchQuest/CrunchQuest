package com.crunchquest.android.ui.components.groupie_views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    private val fragments: ArrayList<Fragment> = ArrayList()
    private val fragmentsTitle: ArrayList<String> = ArrayList()
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentsTitle.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        fragmentsTitle.add(title)

    }

}