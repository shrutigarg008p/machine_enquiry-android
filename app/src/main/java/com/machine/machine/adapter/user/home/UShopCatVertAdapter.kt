package com.machine.machine.adapter.user.home

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.UserItemCateHomeBinding
import com.machine.machine.model.response.user.HomeCategory
import com.machine.machine.ui.user.screen.categoryShop.UShopCategoryTabFragment
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.loadUrl
import kotlinx.android.synthetic.main.item_pics.view.*


class UShopCatVertAdapter(private var itemList: ArrayList<HomeCategory>) :
    RecyclerView.Adapter<BaseAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, UserItemCateHomeBinding::inflate)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as UserItemCateHomeBinding).apply {
            val categoryItem = itemList[position]
            holder.itemView.ivImage.loadUrl(categoryItem.coverImage)
            holder.itemView.tvImageAuthor.text = categoryItem.title
            Log.i("item",categoryItem.title.toString())
            holder.itemView.setOnClickListener {
                FragmentUtil.add(
                    FragmentUtil.sendID(categoryItem.id, UShopCategoryTabFragment()),
                    (holder.itemView.context as FragmentActivity),
                )
            }
        }
    }

    fun addData(itemList: ArrayList<HomeCategory>) {
        val size = itemList.size
        Log.i("cat size",size.toString())
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }
}