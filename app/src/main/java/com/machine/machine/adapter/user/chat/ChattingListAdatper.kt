package com.machine.machine.adapter.user.chat

import Messages
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.R
import com.machine.machine.adapter.seller.sellerRfq.SellerRfqSubListAdapter
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.databinding.UChattingItemBinding
import com.machine.machine.databinding.UChattingRightItemBinding
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.setData
import com.machine.machine.viewmodel.PicsViewModel


class ChattingListAdatper(private var itemList: ArrayList<Messages>, viewModel: PicsViewModel) :
    RecyclerView.Adapter<BaseAdapter>() {
    private val CELL_LEFT = 0
    private val CELL_RIGHT = 1
    var viewModel: PicsViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {

        when (viewType) {
            CELL_RIGHT -> {
                return BaseAdapter.create(parent, UChattingRightItemBinding::inflate)
            }
            CELL_LEFT -> {
                return BaseAdapter.create(parent, UChattingItemBinding::inflate)
            }
        }

        return BaseAdapter.create(parent, UChattingItemBinding::inflate)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        when (holder.itemViewType) {
            CELL_LEFT -> {
                (holder.binding as UChattingItemBinding).let {
                    val item = itemList[position]
//            it.image.loadUrl(item.image)

                    it.msg.setOnClickListener {

                        if(item.message?.type=="1")
                        {
                            val inflater = holder.itemView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            lateinit var sellerRfqSubListAdapter: SellerRfqSubListAdapter
                            val v = inflater.inflate(R.layout.dialog_product,null)

                            viewModel.getrfqdetailsForCustomer(item.message?.text.toString());

                            android.os.Handler().postDelayed({

                               if (viewModel.QutationDetailsForCustomerData.value?.data?.data!=null){
                                sellerRfqSubListAdapter = SellerRfqSubListAdapter(viewModel.QutationDetailsForCustomerData.value?.data?.data!!.items);

                                var d=Dialog(holder.itemView.context)
                                d.setContentView(v)

                                val rv : RecyclerView = v.findViewById(R.id.recyclerview)
                                rv.layoutManager = LinearLayoutManager(holder.itemView.context)
                                rv.setBackgroundColor(holder.itemView.context.getColor(R.color.white))
                                rv.adapter = sellerRfqSubListAdapter

                                d.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                                d.show()
                               }
                            }, 1000)


                        }
                    }

                    it.name.setData(item.user?.name)
                   if(item.message?.type=="1")
                    {
                        it.msg.setTextColor(holder.itemView.context.getColor(R.color.quantum_googblue))

                    }
                    else{
                       it.msg.setTextColor(R.color.black)
                   }


                    it.msg.setData(item.message?.text)
                    it.time.setData(item.message?.createdAtStr)
                }
            }
            CELL_RIGHT -> {
                (holder.binding as UChattingRightItemBinding).let {
                    val item = itemList[position]
//            it.image.loadUrl(item.image)
//                    var re = Regex("^\\d+(\\.\\d+)?\$")


                    it.msg.setOnClickListener {

                        if(item.message?.type=="1")
                        {
                            val inflater = holder.itemView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            lateinit var sellerRfqSubListAdapter: SellerRfqSubListAdapter
                            val v = inflater.inflate(R.layout.dialog_product,null)

                            viewModel.getrfqdetails(item.message?.text.toString());

                            android.os.Handler().postDelayed({
                                if (viewModel.QutationDetailsData.value?.data?.data!=null) {
                                    sellerRfqSubListAdapter =
                                        SellerRfqSubListAdapter(viewModel.QutationDetailsData.value?.data?.data!!.items);

                                    var d = Dialog(holder.itemView.context)
                                    d.setContentView(v)

                                    val rv: RecyclerView = v.findViewById(R.id.recyclerview)
                                    rv.layoutManager = LinearLayoutManager(holder.itemView.context)
                                    rv.setBackgroundColor(holder.itemView.context.getColor(R.color.white))
                                    rv.adapter = sellerRfqSubListAdapter

                                    d.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                                    d.show()
                                }
                            }, 1000)

                        }
                    }
                    if(item.message?.type=="1")
                    {
                        it.msg.setTextColor(holder.itemView.context.getColor(R.color.quantum_googblue))
                    }
                    else
                    {
                        it.msg.setTextColor(R.color.black)
                    }
                    it.name.setData(item.user?.name)
                    it.msg.setData(item.message?.text)
                    it.time.setData(item.message?.createdAtStr)
                }
            }

        }


    }

    override fun getItemCount() = itemList.size

    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]
        return if (item.user?.name.equals("You")
        ) {
            CELL_RIGHT
        } else {
            CELL_LEFT
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun addData(itemList: ArrayList<Messages>) {
        val size = itemList.size
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(size, itemCount)
    }
}