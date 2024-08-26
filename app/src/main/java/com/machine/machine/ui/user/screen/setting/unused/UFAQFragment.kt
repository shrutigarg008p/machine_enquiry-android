package com.machine.machine.ui.user.screen.setting.unused

import android.view.LayoutInflater
import android.view.ViewGroup
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentUFaqBinding

class UFAQFragment : BaseFragmentLoader<FragmentUFaqBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUFaqBinding.inflate(inflater, container, false)


    override fun setup() {
        showContent()
        EventBus.actionBarTitle(getString(R.string.faq))

    }

    override fun onDestroyView() {
        EventBus.hideActionBar()
        super.onDestroyView()
    }

    override fun onRetrybtn() {
        TODO("Not yet implemented")
    }


}
