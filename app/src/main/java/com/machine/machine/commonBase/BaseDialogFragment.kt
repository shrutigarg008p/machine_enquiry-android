package com.machine.machine.commonBase

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.machine.machine.R

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    private var _binding: VB? = null

    val binding get() = _binding!!


    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        _binding = inflateViewBinding(LayoutInflater.from(context))
        val builder = context?.let { MaterialAlertDialogBuilder(it, R.style.RoundShapeTheme) }

        builder?.setView(_binding!!.root)
        viewModelObj()
        viewModelObserver()
        setup()
        viewClick()
        return builder!!.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    abstract fun viewModelObj()
    abstract fun viewModelObserver()
    abstract fun setup()
    abstract fun viewClick()
    abstract fun inflateViewBinding(inflater: LayoutInflater): VB
}