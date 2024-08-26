package com.machine.machine.ui.user.screen.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.amitrawat.postevents.PostEvent
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventConstants
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.EventConstant.EventPage
import com.machine.machine.R
import com.machine.machine.adapter.user.UShippingAddressAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUShoppingAddressBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.AddressDTO
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.AddressVM

/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UShippingAddressFragment : BaseFragramentLoaderVM<FragmentUShoppingAddressBinding>() {
    lateinit var addressListAdapter: UShippingAddressAdapter
    private var addressList: ArrayList<AddressDTO> = ArrayList()
    private lateinit var addressVM: AddressVM
    private var eventItem: EventItem = EventItem()
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUShoppingAddressBinding.inflate(inflater, container, false)

    override fun appHeader() {
        EventBus.showActionBarTitleHideBottomNav(getString(R.string.shipping_address_title))

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        addressVM = ViewModelProvider(this, factory)[AddressVM::class.java]
        addressVM.getAllAddressListApi()
    }

    override fun viewModelObserver() {
        getSubscribeData()
        getAllAddress()
        primaryAddress()
        deleteAddress()
    }

    override fun viewClick() {
        binding.commonBtn.setOnClickListener {
            FragmentUtil.add(UNewAddressFragment(), context as FragmentActivity)
        }
    }

    override fun setup() {

        addressListAdapter = UShippingAddressAdapter(addressList)
        binding.include.rvList.addLinearAdapter(context, addressListAdapter)
    }

    override fun onRetrybtn() {
        addressVM.getAllAddressListApi()

    }

    private fun getAllAddress() {
        addressVM.addressListMLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.data?.data != null) {
                        errorShow(false)
                        addressListAdapter.addData(response.data.data)
                    } else {
                        errorShow(true)
                    }
                }

                is Resource.Error -> {
                    response.errorCode?.let { error ->
                        showError(error)
                    }
                }
                is Resource.Loading -> {
                    showProgess()
                }
            }

        }
    }

    private fun primaryAddress() {
        addressVM.primaryAddressMLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.data?.status == 1) {
                        setPrimaryNotifyAdapter(true)
                    } else {
                        setPrimaryNotifyAdapter(false)
                    }
                }

                is Resource.Error -> {
                    response.message?.let { context?.toast(it) }
                    setPrimaryNotifyAdapter(false)
                }
                is Resource.Loading -> {}
            }

        }
    }


    private fun deleteAddress() {
        addressVM.deleteAddressMLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.data?.status == 1) {
                        deleteNotifyAdapter(true)
                    } else {
                        deleteNotifyAdapter(false)
                    }
                }

                is Resource.Error -> {
                    response.message?.let { context?.toast(it) }
                    deleteNotifyAdapter(false)
                }
                is Resource.Loading -> {}
            }

        }
    }

    private fun deleteNotifyAdapter(isNotError: Boolean) {
        val position: Int = eventItem.position!!
        addressListAdapter.deleteItem(
            position,
            isNotError
        )
    }


    private fun setPrimaryNotifyAdapter(isNotError: Boolean) {
        val position: Int = eventItem.position!!
        addressListAdapter.updatePrimaryAddress(
            position,
            isNotError
        )
    }

    private fun getSubscribeData() {
        PostEvent.subscribe(EventPage.ADDRESS_ACTIVITY, this) {
            it.runAndConsume {
                when (it.id) {
                    EventConstants.EDIT_ADDRESS -> {
                        eventItem = it.value as EventItem
                        //  cartVM.addCardItemApi(eventItem.id!!, 1)
                    }
                    EventConstants.DELETE_ADDRESS -> {
                        eventItem = it.value as EventItem
                        //  addressVM.deleteAddress(eventItem.id!!)
                    }
                    EventConstants.SET_PRIMARY_ADDRESS -> {
                        eventItem = it.value as EventItem
                        addressVM.setPrimaryAddress(eventItem.id!!)

                    }

                }
            }
        }
    }

    private fun errorShow(show: Boolean) {
        showContent()
        if (show) {
            binding.errorDetail.show()
            binding.include.rvList.hide()
        } else {
            binding.errorDetail.hide()
            binding.include.rvList.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.unRegister(EventPage.ADDRESS_ACTIVITY)
        EventBus.onBackCart()
    }
}
