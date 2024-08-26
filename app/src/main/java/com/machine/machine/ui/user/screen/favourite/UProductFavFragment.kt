package com.machine.machine.ui.user.screen.favourite

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.amitrawat.postevents.PostEvent
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventConstants
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.EventConstant.EventPage
import com.machine.machine.adapter.user.favourite.UFavProductAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.commonBase.SpacesItemDecoration
import com.machine.machine.databinding.FragmentUShopFavouriteBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.ProductData
import com.machine.machine.util.addGridVerticalAdapter
import com.machine.machine.util.toast
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.FavouriteVM

/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UProductFavFragment : BaseFragramentLoaderVM<FragmentUShopFavouriteBinding>() {
    private lateinit var favProductAdapter: UFavProductAdapter
    private var favProductList: ArrayList<ProductData> = ArrayList()

    private lateinit var favouriteViewModel: FavouriteVM
    private var eventItem: EventItem = EventItem()
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUShopFavouriteBinding.inflate(inflater, container, false)


    override fun appHeader() {
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        favouriteViewModel = ViewModelProvider(this, factory)[FavouriteVM::class.java]
        favouriteViewModel.getProductFavListApi()
    }

    override fun viewModelObserver() {
        getSubscribeData()
        removedFav()
        favouriteViewModel.productLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1 && response.data.isNotEmpty()) {
                            favProductAdapter.addData(response.data)

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

    override fun setup() {
        verticalAdapter()
    }

    override fun viewClick() {
        binding.refreshLayout.setOnRefreshListener {
            favouriteViewModel.getProductFavListApi()
            binding.refreshLayout.isRefreshing = false
        }

    }

    override fun onRetrybtn() {
        favouriteViewModel.getProductFavListApi()
    }


    private fun verticalAdapter() {
        favProductAdapter = UFavProductAdapter(favProductList)
        binding.include.rvList.addGridVerticalAdapter(activity, 2, favProductAdapter)
        binding.include.rvList.addItemDecoration(SpacesItemDecoration(2, 20, false))
    }


    private fun removedFav() {
        favouriteViewModel.removedFavProductLd.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            if (favProductList.isNotEmpty()) {
                                val position: Int = eventItem.position!!
                                favProductAdapter.removeItem(position, true)

                            } else {
                                getErrorNotifyAdapter()
                            }

                            response.message.let { message ->
                                context?.toast(message)
                            }
                        } else {
                            getErrorNotifyAdapter()
                        }
                    }
                }

                is Resource.Error -> {
                    getErrorNotifyAdapter()
                    response.message?.let { message ->
                        context?.toast(message)
                    }

                }

                is Resource.Loading -> {
                }
            }
        }
    }

    private fun getErrorNotifyAdapter() {
        val position: Int = eventItem.position!!
        favProductAdapter.removeItem(
            position,
            false
        )
    }


    private fun getSubscribeData() {
        PostEvent.subscribe(EventPage.FAVOURITE_ACTIVITY, this) {
            it.runAndConsume {
                when (it.id) {
                    EventConstants.REMOVED_PRODUCT -> {
                        Log.e("sdfs", "msg")
                        eventItem = it.value as EventItem
                        favouriteViewModel.removedFavProductApi(eventItem.id!!)
                    }

                }
            }
        }
    }


    override fun onDestroyView() {
        EventBus.unRegister(EventPage.PRODUCT_FAV_LIST_TAB)
        super.onDestroyView()
    }
}
