package com.machine.machine.ui.user.screen.setting.unused

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentUAboutUsBinding

class UAboutUsFragment : BaseFragmentLoader<FragmentUAboutUsBinding>(), View.OnClickListener {

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUAboutUsBinding.inflate(inflater, container, false)


    override fun setup() {
        showContent()
        EventBus.actionBarTitle(getString(R.string.about_us))

    }


    override fun onRetrybtn() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        EventBus.hideActionBar()
        super.onDestroyView()
    }


    override fun onClick(v: View?) {
        when (v) {
            /*   binding.head.headerBack -> Utils.onBack(activity)
               binding.head.headerNotification -> Utils.onBack(activity)*/

            else -> {

            }
        }
    }
}
