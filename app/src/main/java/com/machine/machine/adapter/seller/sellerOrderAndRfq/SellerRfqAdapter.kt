package com.machine.machine.adapter.seller.sellerOrderAndRfq

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.SellerRfqItemsBinding
import com.machine.machine.model.DataItem

class SellerRfqAdapter : RecyclerView.Adapter<BaseAdapter>() {

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as SellerRfqItemsBinding).apply {
            //bind model to view
            val picItem = differ.currentList[position]
            holder.itemView.apply {
                sellerRfqItemName.text = picItem.author + " "
            }
            holder.itemView.setOnClickListener(View.OnClickListener {
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, SellerRfqItemsBinding::inflate)
    }

    private val differCallback = object : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount() = if (differ.currentList.size > 4) 4 else differ.currentList.size
}