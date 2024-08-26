package com.machine.machine.ui.user.screen.myorder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentUTabviewBinding


/**
 * Created by Amit Rawat on 2/28/2022.
 */
class UMyOrderTabFragment : BaseFragmentLoader<FragmentUTabviewBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUTabviewBinding.inflate(inflater, container, false)


    override fun setup() {
        EventBus.actionBarWithCart(getString(R.string.my_orders))
        init()
        dataNotFound()
    }

    private fun init() {
        // Initializing the ViewPagerAdapter
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.confirmed)))
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.delivered)))
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.pending)))

        val adapter =
            UOrderPagerAdapter(
                childFragmentManager,
                binding.tabs.tabCount,
                binding.customPager
            )


        // Adding the Adapter to the ViewPager
        binding.customPager.adapter = adapter

        binding.customPager.offscreenPageLimit = 1

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                binding.customPager.currentItem = p0!!.position
                adapter.addCount(p0.position)
                // (binding.customPager.adapter as PlansPagerAdapter).notifyDataSetChanged();
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }
        })

    }

    override fun onRetrybtn() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}