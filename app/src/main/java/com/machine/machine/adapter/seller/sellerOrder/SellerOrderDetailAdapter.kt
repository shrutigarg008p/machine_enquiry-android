package com.machine.machine.adapter.seller.sellerOrder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.SellerOrderDetailItemsBinding
import com.machine.machine.model.DataItem
import com.machine.machine.util.loadUrl

class SellerOrderDetailAdapter : RecyclerView.Adapter<BaseAdapter>() {

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as SellerOrderDetailItemsBinding).apply {
            //bind model to view
            val picItem = differ.currentList[position]
            holder.itemView.apply {
                orderDetailItemImage.loadUrl(picItem.download_url)
            }
            holder.itemView.setOnClickListener(View.OnClickListener {
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, SellerOrderDetailItemsBinding::inflate)
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

    override fun getItemCount() = 2
}