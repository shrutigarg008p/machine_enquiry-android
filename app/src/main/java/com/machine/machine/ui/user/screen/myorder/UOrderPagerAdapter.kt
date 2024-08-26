package com.machine.machine.ui.user.screen.myorder

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.machine.machine.commonBase.CustomViewPager


/**
 * Created by Amit Rawat on 2/28/2022.
 */
class UOrderPagerAdapter(fm: FragmentManager, NumOfTabs: Int, viewPager: CustomViewPager) :
    FragmentStatePagerAdapter(fm) {
    var mNumOfTabs: Int
    var vp: CustomViewPager
    var currentItem: Int = 0;
    var onTab: Boolean = true

    override fun getItem(position: Int): Fragment {

        if (onTab) {
            onTab = false
            when (currentItem) {
                0 -> return UConfirmedFragment()
                1 -> return UDeliveredFragment()
                2 -> return UPendingFragment()
            }

        }
        return Fragment()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }


    override fun getCount(): Int {
        return mNumOfTabs
    }

    fun addCount(index: Int) {
        onTab = true
        currentItem = index
        notifyDataSetChanged()
    }


    init {
        mNumOfTabs = NumOfTabs
        vp = viewPager

    }

}