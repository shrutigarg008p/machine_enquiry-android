package com.machine.machine.ui.user.screen.setting

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseDialogFragment
import com.machine.machine.databinding.UItemLogoutDialogBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.setting.SettingVM


class ULogoutDialogFragment : BaseDialogFragment<UItemLogoutDialogBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater) =
        UItemLogoutDialogBinding.inflate(inflater)

    private lateinit var settingVM: SettingVM


    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        settingVM = ViewModelProvider(this, factory)[SettingVM::class.java]
    }

    override fun viewModelObserver() {
        settingVM.logoutVM.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressBtn(false)
                        if (response.data?.status == 1) {
                            EventBus.logOut()
                            context?.toast(getString(R.string.logout_succesfully))
                            dismiss()
                           Utils.getLogin(requireContext())
                        } else {
                            context?.toast(getString(R.string.something_went_wrong))
                        }

                    }

                    is Resource.Error -> {
                        progressBtn(false)
                        response.message?.let { message ->
                            context?.toast(message)
                        }


                    }
                    is Resource.Loading -> {
                        progressBtn(true)
                    }
                }
            }
        }
    }

    override fun setup() {
        binding.title.setData(getString(R.string.logout))
        binding.description.setData(getString(R.string.logoutDescription))

    }

    override fun viewClick() {
        binding.ok.setOnClickListener {

            settingVM.logoutApi()

        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun progressBtn(show: Boolean) {
        if (show) {
            binding.ok.hide()
            binding.progressbtn.show()
        } else {
            binding.ok.show()
            binding.progressbtn.hide()
        }

    }
}
