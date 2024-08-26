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
import com.machine.machine.adapter.user.favourite.UFavShopAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUShopFavouriteBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.ShopDto
import com.machine.machine.util.addLinearAdapter
import com.machine.machine.util.toast
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.FavouriteVM

/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UShopFavFragment : BaseFragramentLoaderVM<FragmentUShopFavouriteBinding>() {
    private lateinit var favShopAdapter: UFavShopAdapter
    private var favouriteShopList: ArrayList<ShopDto> = ArrayList()

    private lateinit var favouriteViewModel: FavouriteVM
    private var eventItem: EventItem = EventItem()
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUShopFavouriteBinding.inflate(inflater, container, false)


    override fun appHeader() {
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        favouriteViewModel = ViewModelProvider(this, factory)[FavouriteVM::class.java]
        favouriteViewModel.getShopFavouriteListApi()
    }

    override fun viewModelObserver() {
        getSubscribeData()
        getAddFavouriteVM()
        favouriteViewModel.shopFavLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.data != null && response.status == 1 && response.data.isNotEmpty()) {
                            favShopAdapter.addData(response.data)

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
            favouriteViewModel.getShopFavouriteListApi()
            binding.refreshLayout.isRefreshing = false
        }

    }

    override fun onRetrybtn() {
        favouriteViewModel.getShopFavouriteListApi()
    }


    private fun verticalAdapter() {
        favShopAdapter = UFavShopAdapter(favouriteShopList)
        binding.include.rvList.addLinearAdapter(context, favShopAdapter)
    }


    private fun getAddFavouriteVM() {
        favouriteViewModel.removeFavShopLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            if (favouriteShopList.isNotEmpty()) {
                                val position: Int = eventItem.position!!
                                favShopAdapter.removeItem(position, true)

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
        favShopAdapter.removeItem(
            position,
            false
        )
    }


    private fun getSubscribeData() {
        PostEvent.subscribe(EventPage.FAVOURITE_ACTIVITY, this) {
            it.runAndConsume {
                when (it.id) {
                    EventConstants.REMOVED_SHOP -> {
                        Log.e("sdfs", "msg")
                        eventItem = it.value as EventItem
                        favouriteViewModel.removedShopFavApi(eventItem.id!!)
                    }

                }
            }
        }
    }


    override fun onDestroyView() {
        EventBus.unRegister(EventPage.FAVOURITE_ACTIVITY)
        super.onDestroyView()
    }
}
