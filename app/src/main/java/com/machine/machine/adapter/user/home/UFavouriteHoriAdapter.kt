package com.machine.machine.adapter.user.home

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.UItemFavHomeBinding
import com.machine.machine.model.response.user.FavouriteShop
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.loadUrl
import kotlinx.android.synthetic.main.item_pics.view.*


class UFavouriteHoriAdapter(private var itemList: ArrayList<FavouriteShop>) :
    RecyclerView.Adapter<BaseAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, UItemFavHomeBinding::inflate)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {
        (holder.binding as UItemFavHomeBinding).apply {
            val item = itemList[position]
            holder.itemView.ivImage.loadUrl(item.shopImage)
            holder.itemView.tvImageAuthor.text = item.shopName
            holder.itemView.setOnClickListener {
                FragmentUtil.shopDetailPage(item.id, holder.itemView.context as FragmentActivity)

            }
        }
    }

    fun addData(itemList: ArrayList<FavouriteShop>) {
        Log.e("size", itemList.size.toString())
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }
}