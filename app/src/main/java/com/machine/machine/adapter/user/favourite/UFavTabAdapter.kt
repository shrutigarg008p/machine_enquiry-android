package com.machine.machine.adapter.user.favourite

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.machine.machine.commonBase.CustomViewPager
import com.machine.machine.ui.user.screen.favourite.UProductFavFragment
import com.machine.machine.ui.user.screen.favourite.UShopFavFragment


/**
 * Created by Amit Rawat on 2/28/2022.
 */
class UFavTabAdapter(fm: FragmentManager, NumOfTabs: Int, viewPager: CustomViewPager) :
    FragmentStatePagerAdapter(fm) {
    var mNumOfTabs: Int
    var vp: CustomViewPager
    var currentItem: Int = 0;
    var onTab: Boolean = true
    override fun getItem(position: Int): Fragment {

        if (onTab) {
            onTab = false
            return when (position) {
                0 -> {
                    UShopFavFragment()
                }
                1 -> {
                    UProductFavFragment()
                }

                else -> Fragment()

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

    override fun saveState(): Parcelable? {
        return null
    }

    init {
        mNumOfTabs = NumOfTabs
        vp = viewPager

    }

}