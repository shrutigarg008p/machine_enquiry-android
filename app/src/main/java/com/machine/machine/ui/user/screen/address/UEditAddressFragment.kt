package com.machine.machine.ui.user.screen.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.constant.BaseConstants
import com.machine.machine.databinding.FragmentUAddShopAddressBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.AddressDTO
import com.machine.machine.network.RequestBodies
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.AddressVM


/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UEditAddressFragment : BaseFragramentLoaderVM<FragmentUAddShopAddressBinding>() {
    private lateinit var addressVM: AddressVM
    private var addressId: Long = 0
    private lateinit var addressData: AddressDTO
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUAddShopAddressBinding.inflate(inflater, container, false)

    override fun appHeader() {
        addressId = FragmentUtil.getIDLong(requireArguments())
        EventBus.showActionBarTitleHideBottomNav(getString(R.string.edit_address))

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        addressVM = ViewModelProvider(this, factory)[AddressVM::class.java]
        addressVM.getAddressById(addressId)
    }

    override fun viewModelObserver() {
        getAddressById()
        updateAddress()
    }

    override fun viewClick() {
        binding.btn.commonBtn.setOnClickListener {
            val body = RequestBodies.Address(
                name = StringUtil.getString(addressData.name),
                email = StringUtil.getString(addressData.email),
                phone = StringUtil.getString(addressData.phone),
                address1 = StringUtil.getString(addressData.address1),
                address2 = StringUtil.getString(addressData.address2),
                city = StringUtil.getString(addressData.city),
                state = StringUtil.getString(addressData.state),
                zip = StringUtil.getString(addressData.zip),
                country = StringUtil.getString(addressData.country),
                latitude = StringUtil.getString(addressData.latitude),
                longitude = StringUtil.getString(addressData.longitude),
            )

            addressVM.updateAddress(body)
        }
    }


    override fun setup() {}

    override fun onRetrybtn() {
        addressVM.getAddressById(addressId)

    }

    private fun getAddressById() {
        addressVM.addressByIdMLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    if (response.data?.data != null) {
                        setUI(response.data.data)

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


    private fun updateAddress() {
        addressVM.updateAddressMLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {

                    Utils.showBtn(binding.btn.progress, binding.btn.commonBtn)
                    binding.btn.progress.errorSnack(
                        response.data?.message.toString(),
                        Snackbar.LENGTH_LONG
                    )
                }

                is Resource.Error -> {
                    Utils.showBtn(binding.btn.progress, binding.btn.commonBtn)
                    response.message?.let { message ->
                        binding.btn.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }
                is Resource.Loading -> {
                    Utils.showProgess(binding.btn.progress, binding.btn.commonBtn)
                }
            }

        }
    }


    fun setUI(obj: AddressDTO) {
        addressData = obj
        binding.title.setData(obj.name)
        binding.description.setData(obj.address1 + BaseConstants.SPACE + obj.address2)
        binding.name.setData(obj.name)
    }


}
