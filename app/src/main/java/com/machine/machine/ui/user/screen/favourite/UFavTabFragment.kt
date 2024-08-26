package com.machine.machine.ui.user.screen.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.machine.machine.R
import com.machine.machine.adapter.user.favourite.UFavTabAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentUTabviewBinding


/**
 * Created by Amit Rawat on 2/28/2022.
 */
class UFavTabFragment : BaseFragmentLoader<FragmentUTabviewBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUTabviewBinding.inflate(inflater, container, false)


    override fun onRetrybtn() {

    }

    override fun setup() {
        tabInit()
        showContent()
    }

    private fun tabInit() {
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.shop)))
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.product)))
        val pagerViewAdapter =
            UFavTabAdapter(
                childFragmentManager,
                binding.tabs.tabCount,
                binding.customPager
            )
        binding.customPager.adapter = pagerViewAdapter
        // binding.customPager.offscreenPageLimit = 1
        binding.tabs.tabMode = TabLayout.MODE_FIXED;

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.customPager.currentItem = tab!!.position
                pagerViewAdapter.addCount(tab.position)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })
    }

}