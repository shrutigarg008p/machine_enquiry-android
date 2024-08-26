package com.machine.machine.adapter.seller.sellerRfq

import android.opengl.Visibility
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.Items
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.NewRfqSubListItemBinding
import com.machine.machine.model.DataItem
import com.machine.machine.model.PicItem
import com.machine.machine.model.response.seller.ProductsDto
import com.machine.machine.model.rfq.Data
import com.machine.machine.util.loadUrl

class SellerRfqSubListAdapter(productsList: ArrayList<Items>) : RecyclerView.Adapter<BaseAdapter>() {

    var productsList: ArrayList<Items> = productsList

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as NewRfqSubListItemBinding).apply {
            //bind model to view
            val picItem = productsList[position]
            holder.binding.rfqSubListItemTitle.setText(picItem.title.toString())
            holder.binding.rfqSubListItemPrice.setText("AED " +picItem.price.toString())
            holder.binding.rfqSubListItemQuantity.setText("Quantity : "+picItem.quantity.toString())
            if(picItem.biddings.size>0)
            {

                holder.binding.rfqSubListItemPriceBidding.visibility= View.VISIBLE
                holder.binding.rfqSubListItemPriceBidding.setText("Bidding : "+picItem.biddings[0].bid.toString())
            }

            holder.itemView.apply {
               rfqSubListItemImage.loadUrl(picItem.image)
            }
            holder.itemView.setOnClickListener(View.OnClickListener {
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, NewRfqSubListItemBinding::inflate)
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

    override fun getItemCount() = productsList.size
}