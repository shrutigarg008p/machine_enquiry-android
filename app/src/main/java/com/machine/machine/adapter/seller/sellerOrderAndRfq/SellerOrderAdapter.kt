package com.machine.machine.adapter.seller.sellerOrderAndRfq

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.SellerOrderItemsBinding
import com.machine.machine.model.DataItem
import com.machine.machine.ui.seller.screen.SellerRfq.SellerRfqTabsFragment
import com.machine.machine.util.Utils
import com.machine.machine.util.loadUrl

class SellerOrderAdapter : RecyclerView.Adapter<BaseAdapter>() {

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as SellerOrderItemsBinding).apply {
            //bind model to view
            val picItem = differ.currentList[position]
            holder.itemView.apply {
                sellerOrderItemImage.loadUrl(picItem.download_url)
                sellerOrderItemDescription.text = picItem.author+" "+ picItem.download_url
            }
            holder.itemView.setOnClickListener(View.OnClickListener {
                Utils.addReplaceFragment(
                    SellerRfqTabsFragment(),
                    (holder.itemView.getContext() as FragmentActivity),
                    IDConst.REPLACE_FRAGMENT)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, SellerOrderItemsBinding::inflate)
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


    override fun getItemCount() = if (differ.currentList.size>4) 4 else differ.currentList.size

}