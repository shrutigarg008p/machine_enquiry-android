package com.machine.machine.commonBase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


class BaseAdapter constructor(val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {


    companion object {

        inline fun create(
            parent: ViewGroup,
            crossinline block: (inflater: LayoutInflater, container: ViewGroup, attach: Boolean) -> ViewBinding
        ) = BaseAdapter(block(LayoutInflater.from(parent.context), parent, false))
    }
}