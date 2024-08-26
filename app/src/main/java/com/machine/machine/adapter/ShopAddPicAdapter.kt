package com.machine.machine.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.model.PicItem
import com.machine.machine.databinding.AdapterAddShopPhotoBinding


class ShopAddPicAdapter(private var picItem: ArrayList<PicItem>) :
    RecyclerView.Adapter<BaseAdapter>() {

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as AdapterAddShopPhotoBinding).apply {
            //bind model to view
            val picItem = picItem[position]
            holder.itemView.apply {
                picLabel.text = picItem.pic_path?.name
                delete.setOnClickListener {
                    deleteItem(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, AdapterAddShopPhotoBinding::inflate)
    }

    override fun getItemCount(): Int {
        return picItem.size
    }

    private fun deleteItem(position: Int) {
        picItem.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, picItem.size)

        //holder.itemView.visibility = View.GONE
    }

}