package com.machine.machine.ui.user.screen.setting.unused

import android.view.LayoutInflater
import android.view.ViewGroup
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUPrivacyBinding

class UPrivacyFragment : BaseFragramentLoaderVM<FragmentUPrivacyBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUPrivacyBinding.inflate(inflater, container, false)

    override fun appHeader() {
        EventBus.actionBarTitle(getString(R.string.privacy_policy))

    }

    override fun viewModelObj() {
    }

    override fun viewModelObserver() {
    }

    override fun viewClick() {
    }

    override fun setup() {
        showContent()

    }

    override fun onDestroyView() {
        EventBus.hideActionBar()

        super.onDestroyView()
    }

    override fun onRetrybtn() {
        TODO("Not yet implemented")
    }


}
