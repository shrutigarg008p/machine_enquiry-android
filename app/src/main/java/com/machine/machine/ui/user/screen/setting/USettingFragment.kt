package com.machine.machine.ui.user.screen.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.FragmentUSettingBinding
import com.machine.machine.model.UserDTO
import com.machine.machine.model.common.Resource
import com.machine.machine.offline.SharedPref
import com.machine.machine.ui.user.screen.NotificationsFragment
import com.machine.machine.ui.user.screen.address.UShippingAddressFragment
import com.machine.machine.ui.user.screen.setting.unused.UAddShopFragment
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.setting.SettingVM


class USettingFragment : BaseFragramentLoaderVM<FragmentUSettingBinding>() {
    private lateinit var settingVM: SettingVM

    private lateinit var users: UserDTO

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUSettingBinding.inflate(inflater, container, false)

    override fun appHeader() {
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        settingVM = ViewModelProvider(this, factory)[SettingVM::class.java]
    }

    override fun viewModelObserver() {

        if(SharedPref.getUserType().equals("customer"))
        {
            binding.settingAddshop.visibility= View.GONE
        }
        else{
            binding.settingAddshop.visibility= View.VISIBLE
        }

        settingVM.getSettingApi()
        settingVM.settingObserver.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    if (response.data?.data != null) {
                        setData(response.data.data)
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

        getNotification()
    }

    fun getNotification() {
        settingVM.notificationVM.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        context?.toast(getString(R.string.notification_update))
                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
                            context?.toast(message)
                        }
                        errorCheckedNotification(binding.notificationToggle)

                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }


    fun getLogout() {
        settingVM.logoutVM.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        context?.toast(getString(R.string.notification_update))
                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
                            context?.toast(message)
                        }


                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }

    private fun errorCheckedNotification(switchCompat: SwitchCompat) {
        if (switchCompat.isChecked) {
            switchCompat.isChecked = false
        } else {
            switchCompat.isChecked = true
        }
    }

    override fun viewClick() {

        binding.let {
            it.aboutusClick.setOnClickListener {
                sendUrl(0)
            }

            it.settingAddshop.setOnClickListener {
                Utils.addReplaceFragment(
                    UAddShopFragment(),
                    (getContext() as FragmentActivity),
                    IDConst.REPLACE_FRAGMENT)
            }

            it.helpSupportClick.setOnClickListener { NextPage(UHelpSupportFragment()) }
            it.logoutClick.setOnClickListener {
               // getLogout()
                val logoutFragment = ULogoutDialogFragment()
                logoutFragment.show(childFragmentManager, BaseConstants.EMPTY)
            }
            it.manageAddressClick.setOnClickListener { NextPage(UShippingAddressFragment()) }
            // it.settingAddshop.setOnClickListener { NextPage(UAddShopFragment()) }
            it.faqClick.setOnClickListener { sendUrl(1) }
            it.privacyClick.setOnClickListener {
                sendUrl(2)
            }
            it.profile.editbtn.setOnClickListener {

            FragmentUtil.EditFragment(users,(getContext() as FragmentActivity))

            }

            it.profile.notification.setOnClickListener{
                NextPage(NotificationsFragment())
            }

            it.languageClick.setOnClickListener {
                EventBus.setLanguage()
            }
            it.notificationToggle.setOnCheckedChangeListener { view, isChecked ->
                if (!view.isPressed) {
                    return@setOnCheckedChangeListener;
                }
                if (isChecked) {
                    settingVM.notificationApi(IDConst.TRUE.toString())
                } else {
                    settingVM.notificationApi(IDConst.FALSE.toString())
                }
            }
            it.settingAccessPermissionSwitch.setOnCheckedChangeListener { _, isChecked ->
                // do whatever you need to do when the switch is toggled here
            }

        }
    }

    override fun setup() {
        if(SharedPref.getUserType().equals("seller"))
        {
            binding.manageAddressClick.hide()
            binding.line1.shopBottomLine.hide()
        }
    }

    private fun sendUrl(urlId: Int) {

        Utils.addReplaceFragment(
            FragmentUtil.sendID(urlId, WebViewFragment()),
            context as FragmentActivity,
            IDConst.ADD_FRAGMENT
        )
    }


    private fun NextPage(fragment: Fragment) {
        Utils.addReplaceFragment(
            fragment,
            context as FragmentActivity,
            IDConst.ADD_FRAGMENT
        )

    }


    override fun onRetrybtn() {
        settingVM.getSettingApi()
    }

    private fun setData(user: UserDTO) {
        if (user.settings != null) {
            binding.notificationToggle.isChecked = user.settings.allowNotification
        } else {
            binding.notificationToggle.isChecked = false
        }

        users=user;
        binding.profile.let {

            it.name.setData(user.name)
            it.email.setData(user.email)
            it.phone.setData(user.phone)
            if (user.profilePic != null) {
                it.pic.loadUrl(user.profilePic)
            }
            if (user.notificationCount!= null && user.notificationCount!=0){
                it.tvNotificationCount.visibility = View.VISIBLE
                it.tvNotificationCount.text = user.notificationCount.toString()
            }else{
                it.tvNotificationCount.visibility = View.GONE
            }

        }
    }


}
