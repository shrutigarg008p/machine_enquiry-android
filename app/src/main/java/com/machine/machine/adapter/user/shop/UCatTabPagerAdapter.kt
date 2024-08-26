package com.machine.machine.adapter.user.shop

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.machine.machine.commonBase.CustomViewPager
import com.machine.machine.ui.user.screen.categoryShop.UShopCategoryTabChildFragment


/**
 * Created by Amit Rawat on 2/28/2022.
 */
class UCatTabPagerAdapter(
    fm: FragmentManager,
    NumOfTabs: Int,
    viewPager: CustomViewPager,
    catId: Long
) :
    FragmentStatePagerAdapter(fm) {
    var mNumOfTabs: Int
    var vp: CustomViewPager
    var currentItem: Int = 0;
    var currentId: Long = 0
    var onTab: Boolean = true

    init {
        currentId = catId
        mNumOfTabs = NumOfTabs
        vp = viewPager

    }

    override fun getItem(position: Int): Fragment {

        if (onTab) {
            onTab = false

            return UShopCategoryTabChildFragment.newInstance(currentId);

        }

        return Fragment()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }


    override fun getCount(): Int {
        return mNumOfTabs
    }

    fun addCount(id: Long) {
        onTab = true
        currentId = id
        notifyDataSetChanged()
    }


}