package com.machine.machine.ui.user.screen.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUAddShopAddressBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.util.Utils
import com.machine.machine.util.errorSnack
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.AddressVM


/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UNewAddressFragment : BaseFragramentLoaderVM<FragmentUAddShopAddressBinding>() {
    private lateinit var addressVM: AddressVM

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUAddShopAddressBinding.inflate(inflater, container, false)

    override fun appHeader() {
        EventBus.showActionBarTitleHideBottomNav(getString(R.string.add_address))

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        addressVM = ViewModelProvider(this, factory)[AddressVM::class.java]
    }

    override fun viewModelObserver() {
        addressMLD()
    }

    override fun viewClick() {
        binding.btn.commonBtn.setOnClickListener {

            /*todo need add the data */

            val body = RequestBodies.Address(
                name = binding.name.text.toString(),
                email = "password",
                phone = binding.name.text.toString(),
                address1 = "",
                address2 = "",
                city = "",
                state = "",
                zip = "",
                country = "",
                latitude = "",
                longitude = "",
            )

            addressVM.addAddress(body)
        }
    }

    override fun setup() {
        showContent()

    }

    override fun onRetrybtn() {
    }

    private fun addressMLD() {
        addressVM.addAddressMLD.observe(this) { response ->
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


}
