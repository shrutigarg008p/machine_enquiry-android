package com.machine.machine.adapter.user.order

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.UItemOrdersBinding
import com.machine.machine.model.DataItem
import com.machine.machine.ui.user.screen.myorder.UOdersDetailFragment
import com.machine.machine.util.Utils
import com.machine.machine.util.loadUrl


class UMyOrderAdapter : RecyclerView.Adapter<BaseAdapter>() {

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as UItemOrdersBinding).apply {
            //bind model to view
            val picItem = differ.currentList[position]
            holder.itemView.apply {
                orderImage.loadUrl(picItem.download_url)

                orderDetailBtn.setOnClickListener(View.OnClickListener {
                    Utils.addReplaceFragment(
                        UOdersDetailFragment(),
                        (holder.itemView.getContext() as FragmentActivity),
                        IDConst.ADD_FRAGMENT
                    )
                })

            }

            holder.itemView.setOnClickListener(View.OnClickListener {
                /* Utils.addReplaceFragment(
                     UProductDetailFragment(),
                     (holder.itemView.getContext() as FragmentActivity),
                     IDConst.ADD_FRAGMENT
                 )*/
            })

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, UItemOrdersBinding::inflate)
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


    override fun getItemCount() = 3

}