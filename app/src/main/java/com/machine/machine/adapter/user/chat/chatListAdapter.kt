package com.machine.machine.adapter.user.chat

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.UChatListBinding
import com.machine.machine.model.response.chat.ChatList
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.setData

class chatListAdapter(private var itemList: ArrayList<ChatList>) :
    RecyclerView.Adapter<BaseAdapter>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {

        return BaseAdapter.create(parent, UChatListBinding::inflate)

    }

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {
        (holder.binding as UChatListBinding).let {
            val item = itemList[position]
//            it.image.loadUrl(item.image)

            val s = item.participants
            var s1 = s?.substring(s.indexOf(", ") + 1)
            s1 = s1?.trim()
           // s1?.trim { it <= ' ' }
            it.title.setData(s1)
            it.time.setData(item.updatedAtStr)
            it.lastMsg.setData(item.last_msg)
            if (item.unread_msg!=0){
                it.unreadMsgCount.setData(item.unread_msg)
            }else{
                it.unreadMsgCount.visibility = View.GONE
            }


            holder.itemView.setOnClickListener {


                FragmentUtil.chattingPage(item.id,"","", holder.itemView.context as FragmentActivity)

            }
        }
    }

    override fun getItemCount() = itemList.size

    fun addData(itemList: ArrayList<ChatList>) {
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }

}