package com.machine.machine.adapter.user

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.BaseConstants
import com.machine.machine.databinding.UItemShippingAddressBinding
import com.machine.machine.model.response.user.AddressDTO
import com.machine.machine.ui.user.screen.address.UEditAddressFragment
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.hide
import com.machine.machine.util.setData
import com.machine.machine.util.show


class UShippingAddressAdapter(private var itemList: ArrayList<AddressDTO>) :
    RecyclerView.Adapter<BaseAdapter>() {

    var primaryAddressPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, UItemShippingAddressBinding::inflate)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {
        (holder.binding as UItemShippingAddressBinding).apply {
            val item = itemList[position]
            holder.itemView.apply {
                title.setData(item.name)
                description.setData(item.address + BaseConstants.SPACE + item.address2)
                if (item.isPrimary) {
                    primaryAddressPosition = position
                }
                primaryAddressCheck.isChecked = item.isPrimary

                deleteProgressBtn(holder.binding, false)

                deleteBtn.setOnClickListener {
                    deleteProgressBtn(holder.binding, true)
                    EventBus.deleteAddress(EventItem(item.id, position))
                }

                editAddress.setOnClickListener {
                    FragmentUtil.add(
                        FragmentUtil.sendID(item.id, UEditAddressFragment()),
                        (holder.itemView.context as FragmentActivity),
                    )
                }

            }

            primaryAddressCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isChecked) {
                    EventBus.setPrimaryAddress(EventItem(item.id, position))
                } else {
                    if (item.isPrimary) {
                        primaryAddressCheck.isChecked = true
                    }
                }
            }
        }

    }


    fun updatePrimaryAddress(position: Int, isNotError: Boolean) {
        if (isNotError) {
            if (itemList.isNotEmpty()) {
                itemList[position].isPrimary = true
                if (primaryAddressPosition != null && primaryAddressPosition != position) {
                    itemList[primaryAddressPosition!!].isPrimary = false
                    notifyItemChanged(primaryAddressPosition!!)
                    primaryAddressPosition = position
                }
            }
        } else {
            itemList[position].isPrimary = primaryAddressPosition == position

        }
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int, isNotError: Boolean) {
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

    private fun deleteProgressBtn(it: UItemShippingAddressBinding, show: Boolean) {
        if (show) {
            it.deleteBtn.hide()
            it.deletePb.show()
        } else {
            it.deleteBtn.show()
            it.deletePb.hide()
        }
    }

    fun addData(itemList: ArrayList<AddressDTO>) {
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }

}