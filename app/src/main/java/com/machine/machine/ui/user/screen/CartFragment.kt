package com.machine.machine.ui.user.screen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.amitrawat.postevents.PostEvent
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventConstants
import com.machine.machine.EventConstant.EventItem
import com.machine.machine.EventConstant.EventPage
import com.machine.machine.R
import com.machine.machine.adapter.user.CartItemListAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.constant.BaseConstants
import com.machine.machine.databinding.FragmentUCartBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.AddressDTO
import com.machine.machine.model.response.user.CartItem
import com.machine.machine.model.response.user.ItemDetail
import com.machine.machine.network.RequestBodies
import com.machine.machine.ui.user.screen.address.UShippingAddressFragment
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.AddressVM
import com.machine.machine.viewmodel.user.CardItemVM

class CartFragment : BaseFragramentLoaderVM<FragmentUCartBinding>() {

    private lateinit var cartVM: CardItemVM
    private lateinit var cartItemAdapter: CartItemListAdapter
    private var cartItemList: ArrayList<CartItem> = ArrayList()
    private var eventItem: EventItem = EventItem()
    private lateinit var addressVM: AddressVM
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUCartBinding.inflate(inflater, container, false)

    override fun appHeader() {
        EventBus.actionBarTitle(getString(R.string.my_Cart))
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        cartVM = ViewModelProvider(this, factory)[CardItemVM::class.java]
        cartVM.getAllCartItemApi()
        addressVM = ViewModelProvider(this, factory)[AddressVM::class.java]
        addressVM.getAllAddressListApi()

    }

    override fun viewModelObserver() {
        getSubscribeData()
        getAllCartItem()
        addQuantity()
        removeQuantity()

    }

    override fun viewClick() {
        binding.refreshLayout.setOnRefreshListener {
            cartVM.getAllCartItemApi()
            binding.refreshLayout.isRefreshing = false
        }

        binding.chat.setOnClickListener {
            EventBus.chatPage()
       }
        binding.changeAddressBtn.setOnClickListener {
            FragmentUtil.add(UShippingAddressFragment(), context as FragmentActivity)
        }
        binding.sendQuote.setOnClickListener {
            sendQuote()
        }
    }

    override fun setup() {
        setAdapter()
    }

    override fun onRetrybtn() {
        cartVM.getAllCartItemApi()
    }

    private fun setAdapter() {
        cartItemAdapter = CartItemListAdapter(cartItemList,context!!)
        binding.rv.addLinearAdapter(context, cartItemAdapter)
    }

    private fun sendQuote() {
        var list = ArrayList<RequestBodies.Items>()
        var check=true;
        for (element in cartItemList) {
            if ((element.bid == null || element.bid == "") && element.priceType.equals("bid")) {
                  check=false;
                break;
            } else {
                list.add(RequestBodies.Items(element.id.toString(), element.bid))
            }
        }

        if(check==true)
        {
            var addressId="";
            addressVM.addressListMLD.observe(this) { response ->
                when (response) {
                    is Resource.Success -> {
                        if (response.data?.data != null && response.data.data.size>0) {
                            addressId= response.data.data[0].id.toString();
                        }
                        else {
                            addressId="0";

                        }
                    }

                    is Resource.Error -> {
                        response.errorCode?.let { error ->
                            showError(error)
                        }
                    }
                    is Resource.Loading -> {
                        showProgess()
                    }
                }

            }
            System.out.print("addressid"+addressId)
            cartVM.callSendQuotationApi(addressId, "delivery", list)

            cartVM.submitQuotation.observe(this) { response ->
                when (response) {
                    is Resource.Success -> {

                        response.message?.let { message ->
                            context?.toast(message)
                        }

                        context?.toast("Product Submit For Quote")
                        FragmentUtil.cartPage(context as FragmentActivity)
                    }

                    is Resource.Error -> {

                        response.message?.let { message ->
                            context?.toast(message)
                        }
                    }

                    is Resource.Loading -> {
                    }
                }

            }
        }
        else
        {
            Toast.makeText(context, "Please enter bid amount", Toast.LENGTH_SHORT).show()

        }



    }

    private fun addQuantity() {
        cartVM.addCartItemLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            setQuantityNotiyAdapter(response.data.item)

                            val totalItem: Int =
                                StringUtil.getInt(response.data.cartItems)
                            val totalPrice: String =
                                StringUtil.getString(response.data.currency) + BaseConstants.SPACE + StringUtil.getString(
                                    response.data.totalSum
                                )

                            totalAmount(totalItem, totalPrice)
                        } else {
                            context?.toast(getString(R.string.something_went_wrong))
                            getErrorNotiyAdapter()
                        }
                    }
                }

                is Resource.Error -> {

                    response.message?.let { message ->
                        context?.toast(message)
                    }
                    getErrorNotiyAdapter()
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun removeQuantity() {
        cartVM.removeCartItemLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            setQuantityNotiyAdapter(response.data.item)

                            val totalItem: Int =
                                StringUtil.getInt(response.data.cartItems)
                            val totalPrice: String =
                                StringUtil.getString(response.data.currency) + BaseConstants.SPACE + StringUtil.getString(
                                    response.data.totalSum
                                )

                            totalAmount(totalItem, totalPrice)
                        } else {
                            context?.toast(getString(R.string.something_went_wrong))
                            getErrorNotiyAdapter()
                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        context?.toast(message)
                    }
                    getErrorNotiyAdapter()

                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun getAllCartItem() {
        cartVM.cartItemListLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1 && response.data.items.isNotEmpty()) {
                            val cartItemList: ArrayList<CartItem> = ArrayList()
                            cartItemList.addAll(response.data.items)
                            cartItemAdapter.addData(cartItemList)

                            val totalItem: Int =
                                StringUtil.getInt(response.data.cartItems)
                            val totalPrice: String =
                                StringUtil.getString(response.data.currency) + BaseConstants.SPACE + StringUtil.getString(
                                    response.data.totalSum
                                )

                            totalAmount(totalItem, totalPrice)

                            if (response.data.primaryAddress != null) {
                                setAddress(
                                    StringUtil.getString(response.data.primaryAddress!!.address1)
                                            + BaseConstants.SPACE + StringUtil.getString(
                                        response.data.primaryAddress!!.address2
                                    )
                                )
                            }
                        } else {
                            emptyCart()

                        }
                    }
                }

                is Resource.Error -> {
                    showError(response.errorCode)

                }

                is Resource.Loading -> {
                    showProgess()
                }
            }
        }
    }

    private fun setQuantityNotiyAdapter(item: ItemDetail) {
        val position: Int = eventItem.position!!
        cartItemAdapter.addRemoveQuantity(
            item,
            position,
            true
        )
    }

    private fun getErrorNotiyAdapter() {
        val position: Int = eventItem.position!!
        cartItemAdapter.addRemoveQuantity(
            null,
            position,
            false
        )
    }

    private fun getSubscribeData() {
        PostEvent.subscribe(EventPage.CART_ACTIVITY, this) {
            it.runAndConsume {
                when (it.id) {
                    EventConstants.ADD_QUANTITY_CART -> {
                        eventItem = it.value as EventItem
                        cartVM.addCardItemApi(eventItem.id!!, 1)
                    }
                    EventConstants.REMOVE_QUANTITY_CART -> {
                        eventItem = it.value as EventItem
                        cartVM.removeCardItemApi(eventItem.id!!, 1)
                    }

                    EventConstants.DELETE_CART -> {
                        eventItem = it.value as EventItem
                        cartVM.removeCardItemApi(eventItem.id!!, -1)
                    }

                    EventConstants.ON_BACK -> {
                        cartVM.getAllCartItemApi()
                    }
                }
            }
        }
    }

    private fun totalAmount(totalItem: Int, totalPrice: String) {
        EventBus.setBadge(totalItem.toString())
        binding.totalItem.setData(totalItem.toString() + BaseConstants.SPACE + getString(R.string.items))
        binding.totalPrice.setData(totalPrice)
    }

    private fun setAddress(address: String) {
        binding.address.setData(address)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.unRegister(EventPage.CART_ACTIVITY)
    }
}