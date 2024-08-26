package com.machine.machine.adapter.seller.sellerRfq

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.NewRfqListItemBinding
import com.machine.machine.model.DataItem
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.seller.ProductsDto
import com.machine.machine.model.rfq.Data
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.seller.screen.SellerRfq.SellerRfqTabsFragment
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.Utils
import com.machine.machine.util.toast
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ProductCategoryVM
import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList

class SellerRfqListAdapter(type: String ,productsList: List<Data>, viewModel: PicsViewModel,productVM:ProductCategoryVM,context: Context?) : RecyclerView.Adapter<BaseAdapter>() {

    var productsList: List<Data> = productsList
    var viewModel: PicsViewModel = viewModel
    var productVM:ProductCategoryVM=productVM

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {

        productsList.distinctBy { data: Data -> data.orderNo }

        (holder.binding as NewRfqListItemBinding).apply {
            //bind model to view
            Log.d("getPictures2",productsList.size.toString())
            val picItem = productsList[position]
            holder.binding.quoteRequestDate.setText(picItem.date.toString())
            holder.binding.quoteRequestId.setText(picItem.orderNo.toString())
            holder.itemView.apply {
            //    quoteRequestName.text = picItem.orderNo + " "
            }

            holder.binding.acceptButton.setOnClickListener {

             // picItem.items[0].biddings[0].id?.let { it1 -> viewModel.bidingProduct(it1.toInt(),1) }
              picItem.orderNo?.let { it1 -> viewModel.acceptRejectQuotationProduct(it1,1) }
              Toast.makeText(context,"Bid Accpect",Toast.LENGTH_SHORT).show()
                Utils.addReplaceFragment(
                    SellerRfqTabsFragment(),
                    (holder.itemView.getContext() as FragmentActivity),
                    IDConst.REPLACE_FRAGMENT)


            }

            holder.binding.denyButton.setOnClickListener {

               // picItem.items[0].biddings[0].id?.let { it1 -> viewModel.bidingProduct(it1,-1) }
                picItem.orderNo?.let { it1 -> viewModel.acceptRejectQuotationProduct(it1,-1) }
                Toast.makeText(context,"Bid Deny",Toast.LENGTH_SHORT).show()
                holder.binding.chatButton.performClick()
               /* Utils.addReplaceFragment(
                    SellerRfqTabsFragment(),
                    (holder.itemView.getContext() as FragmentActivity),
                    IDConst.REPLACE_FRAGMENT)*/

            }

            val layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                true
            )
            newRfqSubRv.setHasFixedSize(true)
            newRfqSubRv.layoutManager = layoutManager
            sellerRfqSubListAdapter = SellerRfqSubListAdapter(picItem.items)
           // sellerRfqSubListAdapter.differ.submitList(differ.currentList)
            newRfqSubRv.adapter = sellerRfqSubListAdapter
            if (type == "new") {
                quotePriceContainer.visibility = View.GONE
                //chatButton.visibility = View.GONE
            } /*else if (type == "progress") {
                quoteAcceptDenyContainer.visibility = View.GONE
            } else {
                quoteAcceptDenyContainer.visibility = View.GONE
                chatButton.visibility = View.GONE
            }*/

           holder.binding.chatButton.setOnClickListener {

               Log.i("value",holder.binding.quoteRequestId.text.toString())
               val value= holder.binding.quoteRequestId.text.toString().split(".").toTypedArray()

               var value1=value[0].toInt();
               var sellerIdList: ArrayList<Int> = ArrayList()
               sellerIdList.add(value1)
               productVM.addChatRequest(sellerIdList)

               android.os.Handler().postDelayed({
                   FragmentUtil.chattingPage(productVM.createChatList.value?.data?.data?.channelId?.toInt(),holder.binding.quoteRequestId.text.toString(),"order" ,holder.itemView.context as FragmentActivity)

               }, 1000)

               //productVM.createChatLi

           }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, NewRfqListItemBinding::inflate)
    }

 /*   private val differCallback = object : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }*/
   // val differ = AsyncListDiffer(this, differCallback)
    var type = type
    lateinit var sellerRfqSubListAdapter: SellerRfqSubListAdapter

    override fun getItemCount() = productsList.size
}


