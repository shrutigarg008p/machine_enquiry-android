package com.machine.machine.adapter.user.notification

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.R
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.ItemUserNotificationBinding
import com.machine.machine.model.response.user.NotificationData
import com.machine.machine.util.getDateInFormat

class NotificationAdapter(private var itemList: ArrayList<NotificationData>,var context:Context): RecyclerView.Adapter<BaseAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, ItemUserNotificationBinding::inflate)
    }

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {
        (holder.binding as ItemUserNotificationBinding).let {
            it.title.text = itemList[position].title
            it.msg.text = itemList[position].body
            it.time.text = getDateInFormat(itemList[position].createdAt.toString(),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","dd MMM YYYY, hh:mm a")
           /* if (itemList[position].status=="1"){
                it.ll.background = ContextCompat.getDrawable(context, R.drawable.notification_bg_unread)
            }*/
        }
    }

    override fun getItemCount(): Int {
        return itemList.size ?: 0
    }
}