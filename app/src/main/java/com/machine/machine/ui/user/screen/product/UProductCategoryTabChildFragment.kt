package com.machine.machine.ui.user.screen.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.amitrawat.postevents.PostEvent
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventConstants
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.EventConstant.EventPage
import com.machine.machine.adapter.user.product.UProductTabCatChildAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.commonBase.SpacesItemDecoration
import com.machine.machine.databinding.FragmentProductTabviewChildBinding
import com.machine.machine.model.common.DataBundle
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.ProductData
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.addGridVerticalAdapter
import com.machine.machine.util.toast
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ProductCategoryVM

/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UProductCategoryTabChildFragment :
    BaseFragramentLoaderVM<FragmentProductTabviewChildBinding>() {

    lateinit var productAdapter: UProductTabCatChildAdapter
    private lateinit var productListVM: ProductCategoryVM

    private var pageSeq: Int = 1
    private var productList: ArrayList<ProductData> = arrayListOf()
    private var eventItem: EventItem = EventItem()
    private lateinit var dataBundle: DataBundle

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProductTabviewChildBinding.inflate(inflater, container, false)

    override fun appHeader() {
        dataBundle = FragmentUtil.getData(requireArguments())

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        productListVM = ViewModelProvider(this, factory)[ProductCategoryVM::class.java]
        productListVM.getProductListById(pageSeq, dataBundle.subCatId!!, dataBundle.shopId!!)
    }

    override fun viewModelObserver() {
        getSubscribeData()
        getShopCategoryListVM()
        getAddFavouriteVM()
    }

    override fun viewClick() {
    }

    override fun onRetrybtn() {
        productListVM.getProductListById(pageSeq, dataBundle.subCatId!!, dataBundle.catId!!)
    }

    override fun setup() {
        verticalAdapter()
    }

    private fun verticalAdapter() {
        productAdapter = UProductTabCatChildAdapter(productList)
        binding.include.rvList.addGridVerticalAdapter(activity, 2, productAdapter)
        binding.include.rvList.addItemDecoration(SpacesItemDecoration(2, 20, false))
    }

    private fun getShopCategoryListVM() {
        productListVM.productListVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1 && response.data.isNotEmpty()) {
                            productAdapter.addData(response.data)
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

    private fun getAddFavouriteVM() {

        productListVM.addFavouriteProductVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            val position: Int = eventItem.position!!
                            response.data?.let {
                                productAdapter.addFav(
                                    it.isFavourite,
                                    position,
                                    true
                                )

                            }
                            response.message.let { message ->
                                context?.toast(message)
                            }
                        } else {
                            getErrorNotiyAdapter()
                        }
                    }
                }

                is Resource.Error -> {
                    getErrorNotiyAdapter()
                    response.message?.let { message ->
                        context?.toast(message)
                    }

                }

                is Resource.Loading -> {
                }
            }
        }
    }

    private fun getErrorNotiyAdapter() {
        val position: Int = eventItem.position!!
        productAdapter.addFav(
            null,
            position,
            false
        )
    }

    private fun getSubscribeData() {
        PostEvent.subscribe(EventPage.PRODUCT_CATEGORY_TAB, this) {
            it.runAndConsume {
                when (it.id) {
                    EventConstants.ADD_REMOVE_FAVOURITE -> {
                        eventItem = it.value as EventItem
                        productListVM.addFavouriteProduct(eventItem.id!!)
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        EventBus.unRegister(EventPage.PRODUCT_CATEGORY_TAB)
        super.onDestroyView()
    }
}
