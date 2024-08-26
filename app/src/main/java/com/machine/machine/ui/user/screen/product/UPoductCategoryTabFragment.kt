package com.machine.machine.ui.user.screen.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.adapter.user.product.UProductCatTabPagerAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUTabviewBinding
import com.machine.machine.model.common.DataBundle
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.CategoryDto
import com.machine.machine.util.FragmentUtil
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ProductCategoryVM


/**
 * Created by Amit Rawat on 2/28/2022.
 */
class UPoductCategoryTabFragment : BaseFragramentLoaderVM<FragmentUTabviewBinding>() {
    private lateinit var subCategoryProduct: ProductCategoryVM
    private lateinit var dataBundle: DataBundle
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUTabviewBinding.inflate(inflater, container, false)


    override fun appHeader() {
        dataBundle = FragmentUtil.getData(requireArguments())
        EventBus.actionBarTitle(getString(R.string.app_name))

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        subCategoryProduct = ViewModelProvider(this, factory)[ProductCategoryVM::class.java]
        subCategoryProduct.subcategoryListAPi(dataBundle.catId!!)
    }

    override fun viewModelObserver() {
        subCategoryProduct.subCategoryListVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1 && response.data.isNotEmpty()) {
                            tabInit(response.data)

                        } else {
                            dataNotFound()

                        }
                    }
                }

                is Resource.Error -> {
                    showError(response.errorCode)

                }

                is Resource.Loading -> {
                    showProgess()
                }
            }
        }
    }

    override fun viewClick() {
    }

    override fun setup() {

    }

    private fun tabInit(categoryList: ArrayList<CategoryDto>) {
        var selectedPosition: Int = 0
        for (item in categoryList.indices) {
            if (dataBundle.subCatId == categoryList[item].id) {
                selectedPosition = item
            }
            binding.tabs.addTab(binding.tabs.newTab().setText(categoryList[item].title))
        }
        val pagerViewAdapter =
            UProductCatTabPagerAdapter(
                childFragmentManager,
                binding.tabs.tabCount,
                binding.customPager, dataBundle.subCatId!!, dataBundle.shopId!!
            )
        binding.customPager.adapter = pagerViewAdapter
        binding.customPager.offscreenPageLimit = 1


        if (binding.tabs.tabCount == 2) {
            binding.tabs.tabMode = TabLayout.MODE_FIXED;
        } else {
            binding.tabs.tabMode = TabLayout.MODE_SCROLLABLE;
        }
        binding.tabs.setScrollX(binding.tabs.width);
        binding.tabs.getTabAt(selectedPosition)?.select();


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {


            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.customPager.currentItem = tab!!.position

                val currentId: Long = categoryList[tab.position].id!!
                pagerViewAdapter.addCount(currentId)

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }
        })

    }


    override fun onRetrybtn() {
        subCategoryProduct.subcategoryListAPi(dataBundle.catId!!)
    }


}