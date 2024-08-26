package com.machine.machine.adapter.user.product

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.machine.machine.commonBase.CustomViewPager
import com.machine.machine.model.common.DataBundle
import com.machine.machine.ui.user.screen.product.UProductCategoryTabChildFragment
import com.machine.machine.util.FragmentUtil


/**
 * Created by Amit Rawat on 2/28/2022.
 */
class UProductCatTabPagerAdapter(
    fm: FragmentManager,
    NumOfTabs: Int,
    viewPager: CustomViewPager,
    subCatId: Long,
    shopId: Long
) :
    FragmentStatePagerAdapter(fm) {
    var mNumOfTabs: Int
    var vp: CustomViewPager
    var currentItem: Int = 0
    var subCategoryId: Long = 0
    var onTab: Boolean = true
    var shopId: Long = 0

    init {
        this.subCategoryId = subCatId
        this.mNumOfTabs = NumOfTabs
        this.vp = viewPager
        this.shopId = shopId
    }

    override fun getItem(position: Int): Fragment {
        if (onTab) {
            onTab = false
            return newInstance();
        }


        return Fragment()
    }


    private fun newInstance(): UProductCategoryTabChildFragment {
        val dataBundle = DataBundle(shopId, null, subCategoryId, null)
        return FragmentUtil.sendData(
            dataBundle,
            UProductCategoryTabChildFragment()
        ) as UProductCategoryTabChildFragment
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }


    override fun getCount(): Int {
        return mNumOfTabs
    }

    fun addCount(subId: Long) {
        onTab = true
        subCategoryId = subId
        notifyDataSetChanged()
    }


}