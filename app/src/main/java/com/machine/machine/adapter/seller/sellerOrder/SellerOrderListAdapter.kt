package com.machine.machine.adapter.seller.sellerOrder

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.provider.TedPermissionProvider
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.NewOrderListItemBinding
import com.machine.machine.model.DataItem
import com.machine.machine.ui.seller.screen.SellerOrder.SellerOrderDetailsFragment
import com.machine.machine.util.Utils

class SellerOrderListAdapter(type: String) : RecyclerView.Adapter<BaseAdapter>() {

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as NewOrderListItemBinding).apply {
            //bind model to view
            val picItem = differ.currentList[position]
            holder.itemView.apply {
                orderUserName.text = picItem.author + " "
            }
            holder.itemView.setOnClickListener(View.OnClickListener {
            })
            orderDetailsButton.setOnClickListener {
                Utils.addReplaceFragment(
                    SellerOrderDetailsFragment(),
                    (holder.itemView.getContext() as FragmentActivity),
                    IDConst.REPLACE_FRAGMENT)
            }

            val layoutManager = LinearLayoutManager(
                TedPermissionProvider.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            newOrderSubRv.setHasFixedSize(true)
            newOrderSubRv.layoutManager = layoutManager
            sellerOrderSubListAdapter = SellerOrderSubListAdapter()
            sellerOrderSubListAdapter.differ.submitList(differ.currentList)
            newOrderSubRv.adapter = sellerOrderSubListAdapter
            if (type == "completed") {
                orderDetailsButton.visibility=View.GONE
            } else if (type == "cancelled") {
                orderDetailsButton.visibility=View.GONE
                orderAddressContainer.visibility = View.GONE
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, NewOrderListItemBinding::inflate)
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
    var type = type
    lateinit var sellerOrderSubListAdapter: SellerOrderSubListAdapter

    override fun getItemCount() = 4
}