package com.machine.machine.adapter.seller.sellerHome

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.IDConst

import com.machine.machine.model.DataItem
import com.machine.machine.model.response.seller.ProductsDto
import com.machine.machine.offline.SharedPref
import com.machine.machine.ui.seller.screen.SellerOrderFragment

import com.machine.machine.util.Utils
import com.machine.machine.util.loadUrl
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.databinding.SellerHomeVerticalItemsBinding
import com.machine.machine.ui.common.OTPActivity
import com.machine.machine.util.FragmentUtil
import kotlinx.android.synthetic.main.seller_home_vertical_items.view.*


class SellerHomeVerticalAdapter(
    productsList: ArrayList<ProductsDto>,
    viewModel: PicsViewModel,
    context: Context?
) :
    RecyclerView.Adapter<BaseAdapter>() {
    var productsList: ArrayList<ProductsDto> = productsList
    var viewModel: PicsViewModel = viewModel
    var context: Context? = context
    var radioValue: String = ""


    fun selectAll() {
        isSelectedAll = true
        notifyDataSetChanged()
    }

    fun unselectall() {
        isSelectedAll = false
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseAdapter, position: Int) {


        (holder.binding as SellerHomeVerticalItemsBinding).apply {
            //bind model to view
            //   val picItem = differ.currentList[position]
            holder.itemView.apply {
                constraint_parent.setOnClickListener {
                   // FragmentUtil.productDetailPage(productsList[position].id!!.toLong(), holder.itemView.context as FragmentActivity)
                   Utils.getProductDetail(holder.itemView.context as FragmentActivity,productsList[position])
                }
                sellerHomeItemImage.loadUrl(productsList[position].image)
                sellerHomeItemTitle.text = productsList[position].title
                price_et.setText(productsList[position].price)
                if(productsList[position].qty.toString()==null)
                {

                    product_qty.setText("")

                }
                else{


                    product_qty.setText(productsList[position].qty.toString())

                }


                var type: String = productsList[position].price_type.toString()

                if (type != null && type.equals("fixed")) {
                    radioFixed.isChecked = true
                    radioValue = "fixed"
                } else if (type != null && type.equals("bid"))  {
                    radioBid.isChecked = true
                    radioValue = "bid"
                }

            }

            holder.binding.cvremoveShop.setOnClickListener(View.OnClickListener {

                var shopId: String? = SharedPref.getShopId()
                viewModel.removeProduct(
                    productsList[position].id!!,
                    Integer.parseInt(shopId)
                )
                Toast.makeText(context, "Product Remove Successfully", Toast.LENGTH_SHORT)
                    .show()

            })

            holder.binding.cvAddShop.setOnClickListener(View.OnClickListener {

                if (SharedPref.getShopId() == null) {
                    Toast.makeText(context, "Please select Shops for updating", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    if(radioFixed.isChecked)
                    {
                        radioValue = "fixed"
                    }
                    else
                    {
                        radioValue = "bid"
                    }


                    var shopId: String? = SharedPref.getShopId()
                    viewModel.updateProduct(
                        productsList[position].id!!,
                        holder.binding.productQty.text.toString(),
                        radioValue,
                        holder.binding.priceEt.text.toString(),
                        Integer.parseInt(shopId)
                    )

                    Toast.makeText(context, "Product Updated Successfully", Toast.LENGTH_SHORT)
                        .show()

                }
            })
          /*  holder.itemView.setOnClickListener(View.OnClickListener {
                Utils.addReplaceFragment(
                    SellerOrderFragment(),
                    (holder.itemView.getContext() as FragmentActivity),
                    IDConst.REPLACE_FRAGMENT
                )
            })*/
            if (!isSelectedAll) {
                sellerHomeItemCheckbox.setChecked(false);
            } else sellerHomeItemCheckbox.setChecked(true);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter {
        return BaseAdapter.create(parent, SellerHomeVerticalItemsBinding::inflate)
    }

    private var isSelectedAll = false
    private val differCallback = object : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun getItemCount() = productsList.size

}