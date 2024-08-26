package com.machine.machine.adapter.user.product

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.R
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.ItemProductCategoryBinding
import com.machine.machine.model.response.user.ProductData
import com.machine.machine.util.*


class UProductTabCatChildAdapter(private var itemList: ArrayList<ProductData>) :
    RecyclerView.Adapter<BaseAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, ItemProductCategoryBinding::inflate)
    }

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as ItemProductCategoryBinding).let {
            val item = itemList[position]
            it.productImg.loadUrl(item.image)
            it.productName.setData(item.title)
            it.productCurrency.setData(item.currency)
            it.productPrice.setData(item.price)
            if (item.isFavourite != null && item.isFavourite == true) {
                it.productFav.setColorFilter(holder.itemView.context.getColor(R.color.btnOrange))
            } else {
                it.productFav.setColorFilter(holder.itemView.context.getColor(R.color.dark_grey))
            }
            progressBtn(holder.binding, false)
            it.productFav.setOnClickListener {
                progressBtn(holder.binding, true)
                EventBus.addRemoveProductFav(EventItem(item.id, position))
            }
            holder.itemView.setOnClickListener {
                FragmentUtil.productDetailPage(item.id, holder.itemView.context as FragmentActivity)

            }

        }

    }


    fun progressBtn(it: ItemProductCategoryBinding, show: Boolean) {
        if (show) {
            it.productFav.hide()
            it.favProgress.show()
        } else {
            it.productFav.show()
            it.favProgress.hide()
        }

    }


    fun addData(itemList: ArrayList<ProductData>) {
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }


    fun addFav(fav: Boolean?, position: Int, isNotError: Boolean) {
        if (isNotError) {
            if (itemList.isNotEmpty()) {
                itemList[position].isFavourite = fav!!

            }
        }
        notifyItemChanged(position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = itemList.size


}