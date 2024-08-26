package com.machine.machine.ui.user.screen.product.detail

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.UItemAdditionalInfoBinding
import com.machine.machine.model.response.user.AdditionalInfo
import com.machine.machine.util.setData


class AdditionalInfoAdapter(private var itemList: ArrayList<AdditionalInfo>) :
    RecyclerView.Adapter<BaseAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, UItemAdditionalInfoBinding::inflate)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {
        (holder.binding as UItemAdditionalInfoBinding).apply {
            val item = itemList[position]
            label.setData(item.key)
            value.setData(item.value)
        }
    }

    fun addData(itemList: ArrayList<AdditionalInfo>) {
        Log.e("size", itemList.size.toString())
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }
}