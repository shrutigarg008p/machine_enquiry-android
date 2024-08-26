package com.machine.machine.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.machine.machine.commonBase.BaseFragment
import com.machine.machine.databinding.ActivityMainBinding

class EmptyFragment : BaseFragment<ActivityMainBinding>() {


    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ActivityMainBinding.inflate(inflater, container, false)


    override fun setup() {

    }

}