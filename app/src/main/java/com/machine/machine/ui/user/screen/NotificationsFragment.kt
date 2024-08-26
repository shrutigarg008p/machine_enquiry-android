package com.machine.machine.ui.user.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.adapter.user.notification.NotificationAdapter
import com.machine.machine.commonBase.BaseFragment
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentNotificationsBinding
import com.machine.machine.databinding.FragmentUNotificationBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.NotificationData
import com.machine.machine.offline.SharedPref
import com.machine.machine.util.addLinearAdapter
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.setting.SettingVM

class NotificationsFragment : BaseFragramentLoaderVM<FragmentNotificationsBinding>() {
    private lateinit var notificationAdapter: NotificationAdapter
    private var notificationList: ArrayList<NotificationData> = ArrayList()
    private lateinit var settingVM: SettingVM

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentNotificationsBinding.inflate(inflater, container, false)


    override fun setup() {
        /* notificationsViewModel =
             ViewModelProvider(this).get(NotificationsViewModel::class.java)
         notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
             binding.textDashboard.text = it
         })*/

        // verticalAdapter()
    }

    override fun appHeader() {
        EventBus.showActionBarTitleHideBottomNav(getString(R.string.notification))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.hideActionBarTitleShowBottomNav()
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        settingVM = ViewModelProvider(this, factory)[SettingVM::class.java]
    }

    override fun viewModelObserver() {
        if(SharedPref.getUserType().equals("customer"))
        {
            settingVM.getCustomerNotificationApi()
        }
        else{
            settingVM.getSellerNotificationApi()
        }

        settingVM.userNotificationVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    if (response.data?.data != null) {
                        if (response.data.data.size>0){
                            notificationList = response.data.data
                            verticalAdapter()
                            settingVM.getCustomerNotificationReadApi()
                        }
                    } else {
                        dataNotFound()
                    }
                }

                is Resource.Error -> {
                    //  Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                    response.errorCode?.let { error ->
                        showError(error)
                    }
                }
                is Resource.Loading -> {
                    showProgess()
                }
            }

        }
        settingVM.userNotificationReadVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                }

                is Resource.Error -> {
                    //  Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                    response.errorCode?.let { error ->
                        showError(error)
                    }
                }
                is Resource.Loading -> {
                    showProgess()
                }
            }

        }

        settingVM.sellerNotificationVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    if (response.data?.data != null) {
                        if (response.data.data.size>0){
                            notificationList = response.data.data
                            verticalAdapter()
                            settingVM.getSellerNotificationReadApi()
                        }
                    } else {
                        dataNotFound()
                    }
                }

                is Resource.Error -> {
                    //  Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                    response.errorCode?.let { error ->
                        showError(error)
                    }
                }
                is Resource.Loading -> {
                    showProgess()
                }
            }

        }
        settingVM.sellerNotificationReadVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                }

                is Resource.Error -> {
                    //  Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
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

    private fun verticalAdapter() {
        notificationAdapter = NotificationAdapter(notificationList,requireContext())
        binding.rvCustomerNotification.addLinearAdapter(activity, notificationAdapter)
    }

    override fun viewClick() {

    }

    override fun onRetrybtn() {

    }
}
