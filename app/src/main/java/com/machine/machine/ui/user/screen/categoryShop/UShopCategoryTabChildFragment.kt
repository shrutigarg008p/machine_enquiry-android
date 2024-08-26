package com.machine.machine.ui.user.screen.categoryShop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.amitrawat.postevents.PostEvent
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventConstants
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.EventConstant.EventPage
import com.machine.machine.R
import com.machine.machine.adapter.user.shop.UShopTabCatChildAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.FragmentShopcategoryTabviewChildBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.ShopDto
import com.machine.machine.util.addLinearAdapter
import com.machine.machine.util.toast
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ShopCategoryVM

/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UShopCategoryTabChildFragment :
    BaseFragramentLoaderVM<FragmentShopcategoryTabviewChildBinding>() {
    lateinit var childShopCategoryAdapter: UShopTabCatChildAdapter
    private lateinit var categoryShop: ShopCategoryVM
    private var categoryId: Long = 0
    private var pageSeq: Int = 1
    private var shopsListing: ArrayList<ShopDto> = arrayListOf()
    private var eventItem: EventItem = EventItem()
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentShopcategoryTabviewChildBinding.inflate(inflater, container, false)

    companion object {
        fun newInstance(CategoryID: Long): UShopCategoryTabChildFragment {
            val args = Bundle()
            args.putLong(IDConst.SHOP_ID, CategoryID)
            val fragment = UShopCategoryTabChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun appHeader() {
        categoryId = requireArguments().getLong(IDConst.SHOP_ID)

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        categoryShop = ViewModelProvider(this, factory)[ShopCategoryVM::class.java]
        categoryShop.getShopCategoryListById(pageSeq, categoryId)
    }

    override fun viewModelObserver() {
        getSubscribeData()
        getShopCategoryListVM()
        getAddFavouriteVM()

    }

    override fun viewClick() {
    }

    override fun onRetrybtn() {
        categoryShop.getShopCategoryListById(pageSeq, categoryId)
    }

    override fun setup() {

        verticalAdapter()
    }

    private fun verticalAdapter() {

        childShopCategoryAdapter = UShopTabCatChildAdapter(requireActivity(),shopsListing)
        binding.include.rvList.addLinearAdapter(context, childShopCategoryAdapter)
        binding.include.rvList.itemAnimator = DefaultItemAnimator()

    }

    private fun getShopCategoryListVM() {
        categoryShop.shopCategoryListVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1 && response.data.isNotEmpty()) {
                            childShopCategoryAdapter.addData(response.data)
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

        categoryShop.addFavouriteShopVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            val position: Int = eventItem.position!!
                            response.data?.let {
                                childShopCategoryAdapter.addFav(
                                    it.isFavourite,
                                    position,
                                    true
                                )

                            }
                            response.message.let { message ->
                                context?.toast(message)
                            }
                        } else {
                            context?.toast(getString(R.string.something_went_wrong))
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
        childShopCategoryAdapter.addFav(
            null,
            position,
            false
        )
    }

    private fun getSubscribeData() {
        PostEvent.subscribe(EventPage.SHOP_CATEGORY_TAB, this) {
            it.runAndConsume {
                when (it.id) {
                    EventConstants.ADD_REMOVE_FAVOURITE -> {
                        Log.e("msg", "msg")
                        eventItem = it.value as EventItem
                        categoryShop.addFavouriteShop(eventItem.id!!)
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        EventBus.unRegister(EventPage.SHOP_CATEGORY_TAB)
        super.onDestroyView()
    }
}
