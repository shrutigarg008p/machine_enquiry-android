package com.machine.machine.adapter.seller.sellerRfq

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.machine.machine.commonBase.CustomViewPager

class SellerRfqTabAdapter(fm: FragmentManager, viewPager: CustomViewPager) :
    FragmentStatePagerAdapter(fm) {
    var vp: CustomViewPager
    var currentItem: Int = 0

    // declare arrayList to contain fragments and its title
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        // return a particular fragment page
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        // return the number of tabs
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        // return title of the tab
        return mFragmentTitleList[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun addCount(index: Int) {
        currentItem = index
        notifyDataSetChanged()
    }

    fun addFragment(fragment: Fragment, title: String) {
        // add each fragment and its title to the array list
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    init {
        vp = viewPager
    }
}