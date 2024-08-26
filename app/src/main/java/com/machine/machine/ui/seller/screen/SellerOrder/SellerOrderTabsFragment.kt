package com.machine.machine.ui.seller.screen.SellerOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.machine.machine.R
import com.machine.machine.adapter.seller.sellerRfq.SellerRfqTabAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentSellerRfqTabsBinding


class SellerOrderTabsFragment : BaseFragmentLoader<FragmentSellerRfqTabsBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSellerRfqTabsBinding.inflate(inflater, container, false)


    override fun setup() {
        init()
       dataNotFound()
    }

    private fun init() {
        binding.sellerRfqTabs.addTab(
            binding.sellerRfqTabs.newTab().setText(getString(R.string.new_title))
        )
        binding.sellerRfqTabs.addTab(
            binding.sellerRfqTabs.newTab().setText(getString(R.string.completed))
        )
        binding.sellerRfqTabs.addTab(
            binding.sellerRfqTabs.newTab().setText(getString(R.string.cancelled))
        )
        val pagerViewAdapter =
            SellerRfqTabAdapter(
                requireActivity().supportFragmentManager,
                binding.sellerRfqPager
            )
        pagerViewAdapter.addFragment(NewOrderListFragment(), "New")
        pagerViewAdapter.addFragment(CompletedOrderListFragment(), "Completed")
        pagerViewAdapter.addFragment(CancelledOrderListFragment(), "Cancelled")

        binding.sellerRfqPager.adapter = pagerViewAdapter
        binding.sellerRfqPager.offscreenPageLimit = 1
        binding.sellerRfqTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                binding.sellerRfqPager.currentItem = p0!!.position
                pagerViewAdapter.addCount(p0.position)
                // (binding.customPager.adapter as PlansPagerAdapter).notifyDataSetChanged();
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }
        })
    }

    override fun onRetrybtn() {
        TODO("Not yet implemented")
    }
}