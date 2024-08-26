package com.machine.machine.ui.user.screen.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUChangePasswordBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.setting.SettingVM

class UChangePasswordFragment : BaseFragramentLoaderVM<FragmentUChangePasswordBinding>(),
    View.OnClickListener {
    private lateinit var settingVM: SettingVM
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUChangePasswordBinding.inflate(inflater, container, false)

    override fun appHeader() {
        EventBus.actionBarTitle(getString(R.string.change_password_label))
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        settingVM = ViewModelProvider(this, factory)[SettingVM::class.java]
    }

    override fun viewModelObserver() {
        settingVM.changePasswordLiveData.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                        context?.toast(getString(R.string.password_reset_successfully))
                        EventBus.onBack()
                    }

                    is Resource.Error -> {
                        Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                        response.message?.let { message ->
                            binding.reset.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }
                    is Resource.Loading -> {
                        Utils.showProgess(binding.reset.progress, binding.reset.commonBtn)
                    }
                }
            }
        }
    }

    override fun viewClick() {
        binding.reset.commonBtn.setOnClickListener(this)
        binding.showPassBtn.setOnClickListener(this)
        binding.showRePassBtn.setOnClickListener(this)
        binding.oldShowPassBtn.setOnClickListener(this)
    }

    override fun setup() {
        showContent()
    }


    override fun onRetrybtn() {
        sumitRequest()
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.reset.commonBtn -> sumitRequest()
            binding.showPassBtn -> Utils.HideShowPassword(binding.password, binding.showPassBtn)
            binding.showRePassBtn -> Utils.HideShowPassword(
                binding.passwordConfirm,
                binding.showRePassBtn
            )
            binding.oldShowPassBtn -> Utils.HideShowPassword(
                binding.oldpassword,
                binding.oldShowPassBtn
            )
            else -> {

            }
        }
    }

    fun sumitRequest() {
        val oldPassword = binding.oldpassword.text.toString()
        val password = binding.password.text.toString()
        val confirm_password = binding.passwordConfirm.text.toString()


        if (oldPassword.isEmpty()) {
            context?.let { AlertUtil.ok(it, getString(R.string.please_enter_old_passwor)) }
            return
        }
        if (!ValidationUtil.checkPassword(getActivity(), password, confirm_password)) {
            return
        }

        val body = RequestBodies.changePassword(
            oldPassword,
            password,
            confirm_password
        )

        settingVM.changePasswordApi(body)
    }


}
