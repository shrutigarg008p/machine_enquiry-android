package com.machine.machine.adapter.user.favourite

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.R
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.UItemFavProductBinding
import com.machine.machine.model.response.user.ProductData
import com.machine.machine.util.*


class UFavProductAdapter(private var itemList: ArrayList<ProductData>) :
    RecyclerView.Adapter<BaseAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, UItemFavProductBinding::inflate)
    }

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {
        Log.e("postin", position.toString())

        (holder.binding as UItemFavProductBinding).let {
            val item = itemList[position]
            it.productImg.loadUrl(item.image)
            it.productName.setData(item.title)
            it.productCurrency.setData(item.currency)
            it.productPrice.setData(item.price)
            if (item.isFavourite == null) {
                it.productFav.setColorFilter(holder.itemView.context.getColor(R.color.btnOrange))
            }
            it.productFav.setOnClickListener {
                Log.e("clicking", position.toString())
                progressBtn(holder.binding, true)
                EventBus.removedProductFav(EventItem(item.id, position))
            }
            holder.itemView.setOnClickListener {


                FragmentUtil.productDetailPage(item.id, holder.itemView.context as FragmentActivity)

            }

        }

    }


    fun progressBtn(it: UItemFavProductBinding, show: Boolean) {
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

    fun removeItem(position: Int, isNotError: Boolean) {
        if (isNotError) {
            if (itemList.isNotEmpty()) {
                if (itemList.size != position) {
                    itemList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount);
                }
            } else {
                notifyItemChanged(position)
            }
        } else {
            notifyItemChanged(position)
        }


    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = itemList.size


}