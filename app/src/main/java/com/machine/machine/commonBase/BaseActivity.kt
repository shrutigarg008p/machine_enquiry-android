package com.machine.machine.commonBase

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.machine.machine.localization.LocalizationActivity

abstract class BaseActivity<VB : ViewBinding> : LocalizationActivity() {
    private var _binding: VB? = null

    protected val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateLayout(layoutInflater)
        setContentView(_binding!!.root)
        viewModelObj()
        setup()

    }

    abstract fun inflateLayout(layoutInflater: LayoutInflater): VB
    abstract fun viewModelObj()
    abstract fun setup()


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}