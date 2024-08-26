package com.machine.machine.adapter.user

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.R
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.UItemCartListBinding
import com.machine.machine.model.response.user.CartItem
import com.machine.machine.model.response.user.ItemDetail
import com.machine.machine.util.*


class CartItemListAdapter(private var itemList: ArrayList<CartItem>,var context: Context) :
    RecyclerView.Adapter<BaseAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, UItemCartListBinding::inflate)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        (holder.binding as UItemCartListBinding).let {
            val item = itemList[position]
            it.image.loadUrl(item.image)
            it.title.setData(item.title)
            val currencyPrice: String =
                StringUtil.getString(item.currency) + BaseConstants.SPACE + StringUtil.getString(
                    item.price
                )
            it.currencyPrice.setData(currencyPrice)
            itemList[position].bid = it.bid.editableText.toString()
            it.bid.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable) {

                    if (s.toString().length == 1 && s.toString().startsWith("0")) {
                        s.clear()
                    }else if(s.toString().isNotEmpty() && s.toString().toFloat()>=item.price.toFloat()){
                        it.bid.setText("")
                        Toast.makeText(context,"Bid must be less than product price",Toast.LENGTH_SHORT).show()
                    }else {
                        itemList[position].bid = s.toString()
                    }
                }
            })


            addProgressBtn(holder.binding, false)
            removeProgressBtn(holder.binding, false)
            deleteProgressBtn(holder.binding, false)
            it.quantity.setData(item.quantity)

            if (item.priceType == IDConst.PRICE_BID) {
                it.currencyPrice.hide()
                it.bid.show()
                it.bid.text?.clear()
            } else {
                it.currencyPrice.show()
                it.bid.hide()
                it.bid.text?.clear()
            }

            it.add.setOnClickListener {
                addProgressBtn(holder.binding, true)
                EventBus.addCartQuanity(EventItem(item.id, position))
            }
            it.remove.setOnClickListener {
                if (item.quantity > 1) {
                    removeProgressBtn(holder.binding, true)
                    EventBus.removeCartQuanity(EventItem(item.id, position))
                } else {
                    holder.itemView.context.let {
                        it.toast(it.getString(R.string.product_quantity_minimum_1))
                    }
                }

            }

            it.delete.setOnClickListener {
                deleteProgressBtn(holder.binding, true)
                EventBus.deleteCart(EventItem(item.id, position))
            }
        }
    }

    private fun addProgressBtn(it: UItemCartListBinding, show: Boolean) {
        if (show) {
            it.add.hide()
            it.addPb.show()
        } else {
            it.add.show()
            it.addPb.hide()
        }
    }

    private fun removeProgressBtn(it: UItemCartListBinding, show: Boolean) {
        if (show) {
            it.remove.hide()
            it.removePb.show()
        } else {
            it.remove.show()
            it.removePb.hide()
        }
    }

    private fun deleteProgressBtn(it: UItemCartListBinding, show: Boolean) {
        if (show) {
            it.delete.hide()
            it.deletePb.show()
        } else {
            it.delete.show()
            it.deletePb.hide()
        }
    }

    fun addData(itemList: ArrayList<CartItem>) {
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }


    fun addRemoveQuantity(item: ItemDetail?, position: Int, isNotError: Boolean) {
        if (item != null) {
            if (isNotError) {
                if (itemList.isNotEmpty()) {
                    itemList[position].quantity = item.quantity
                    itemList[position].totalAmount = item.totalAmount

                }
            }
            notifyItemChanged(position)
        } else {
            removeItem(position, isNotError)
        }
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
}