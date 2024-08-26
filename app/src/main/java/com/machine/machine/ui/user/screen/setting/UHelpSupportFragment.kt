package com.machine.machine.ui.user.screen.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUHelpSupportBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.util.AlertUtil
import com.machine.machine.util.Utils
import com.machine.machine.util.errorSnack
import com.machine.machine.util.toast
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.setting.SettingVM

class UHelpSupportFragment : BaseFragramentLoaderVM<FragmentUHelpSupportBinding>() {
    private lateinit var settingVM: SettingVM
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUHelpSupportBinding.inflate(inflater, container, false)

    override fun appHeader() {

        EventBus.actionBarTitle(getString(R.string.help_support))
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        settingVM = ViewModelProvider(this, factory)[SettingVM::class.java]
    }

    override fun viewModelObserver() {
        settingVM.helpSupportLiveData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.btn.progress, binding.btn.commonBtn)
                        context?.toast(getString(R.string.message_send_successfully))
                        EventBus.onBack()
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

    override fun setup() {
        showContent()
        binding.btn.commonBtn.text = getResources().getString(R.string.send_message)


    }

    override fun viewClick() {
        binding.btn.commonBtn.setOnClickListener {
            try {
                sumitRequest()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
    }


    override fun onRetrybtn() {
    }


    private fun sumitRequest() {
        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val message = binding.msg.text.toString()

        if (name.isEmpty()) {
            context?.let { AlertUtil.ok(it, getString(R.string.please_enter_name)) }
            return
        }
        if (email.isEmpty()) {
            context?.let { AlertUtil.ok(it, getString(R.string.please_enter_email)) }
            return
        }
        if (message.isEmpty()) {
            context?.let { AlertUtil.ok(it, getString(R.string.please_enter_msg)) }
            return
        }


        val body = RequestBodies.helpAndSupport(
            name,
            email,
            message
        )

        settingVM.helpAndSupportApi(body)
    }


}
