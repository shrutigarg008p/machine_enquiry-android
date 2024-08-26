package com.machine.machine.adapter.user.shop

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.ItemCategoryBinding
import com.machine.machine.model.common.DataBundle
import com.machine.machine.model.response.user.ProductCategories
import com.machine.machine.ui.user.screen.product.UPoductCategoryTabFragment
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.loadUrl


class UShopDetailProductAdapter(
    private var itemList: ArrayList<ProductCategories>,
    private var shopId: Long,
    private var catId: Long

) :
    RecyclerView.Adapter<BaseAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, ItemCategoryBinding::inflate)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as ItemCategoryBinding).apply {
            val item = itemList[position]
            holder.itemView.apply {

                ivImage.loadUrl(item.coverImage)
                tvImageAuthor.text = item.title
            }
            holder.itemView.setOnClickListener {
                Log.d("shopId",shopId.toString());
                Log.d("parentid",item.id.toString());
               Log.d("cat",catId.toString());


                   val dataBundle = DataBundle(shopId, catId, item.id, null)
                FragmentUtil.add(
                    FragmentUtil.sendData(dataBundle, UPoductCategoryTabFragment()),
                    (holder.itemView.context as FragmentActivity),
               )

            }
        }


    }

    fun addShopId(shopId: Long?) {
        this.shopId = shopId!!
    }


    fun addData(itemList: ArrayList<ProductCategories>) {
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }


}