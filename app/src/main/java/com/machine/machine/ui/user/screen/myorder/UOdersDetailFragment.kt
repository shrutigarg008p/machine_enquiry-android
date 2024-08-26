package com.machine.machine.ui.user.screen.myorder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentUOrdersDetailsBinding


/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UOdersDetailFragment : BaseFragmentLoader<FragmentUOrdersDetailsBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUOrdersDetailsBinding.inflate(inflater, container, false)


    override fun setup() {
        EventBus.actionBarTitle(getString(R.string.order_details))
        showContent()


    }


    override fun onRetrybtn() {

    }


    override fun onDestroyView() {

        super.onDestroyView()
    }
}
