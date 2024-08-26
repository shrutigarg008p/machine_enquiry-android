package com.machine.machine.ui.user.screen.chat

import Messages
import android.net.Uri
import android.opengl.Visibility
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.machine.machine.adapter.user.chat.ChattingListAdatper
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUChattingBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.util.FragmentUtil
import com.machine.machine.util.addGridVerticalAdapter
import com.machine.machine.util.addLinearAdapter
import com.machine.machine.util.changeFavBtn
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ChattingVM
import com.machine.machine.viewmodel.user.ProductCategoryVM
import kotlinx.android.synthetic.main.u_chat_list.view.*
import kotlin.collections.ArrayList
import kotlin.math.log


class UChattingFragment : BaseFragramentLoaderVM<FragmentUChattingBinding>() {
    private lateinit var chattingVM: ChattingVM
    private lateinit var chatListAdapter: ChattingListAdatper

    private lateinit var viewModel: PicsViewModel

    private var favProductList: ArrayList<Messages> = ArrayList()
    private var messageString: String = ""
    private var chatId: Int = 0
    private var productId: String? = ""
    private var type: String? = ""

    private lateinit var productVM: ProductCategoryVM


    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUChattingBinding.inflate(inflater, container, false)

    override fun appHeader() {
        chatId = FragmentUtil.getIdInt(requireArguments())
        productId=FragmentUtil.getProductId(requireArguments())
        type=FragmentUtil.getType(requireArguments());

        Log.i("type",type.toString());

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        chattingVM = ViewModelProvider(this, factory)[ChattingVM::class.java]

        val factory1 = ViewModelProviderFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory1)[PicsViewModel::class.java]



        Log.e("type_new",type.toString());
        if(type=="product")
        {
            productVM = ViewModelProvider(this, factory)[ProductCategoryVM::class.java]
            productId?.let { productVM.getProductDetailApi(it.toLong()) }
            productDetailVM();
            binding.productlayout.visibility = View.VISIBLE

        }
        else
        {
            if(type=="order")
             {
                 chattingVM.sendMessage(productId.toString(), chatId,1)
                 chattingVM.sendMessageLiveData.observe(this) { response ->
                     when (response) {
                         is Resource.Success -> {
                             showContent()
                             if (response.data?.status == 1) {
                                 chattingVM.getChat(chatId)
                                 chatListAdapter.notifyDataSetChanged()
                                 binding.messageEt.setText("")
                             } else {
                                 dataNotFound()
                             }
                         }

                         is Resource.Error -> {
                             //  Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                             response.errorCode?.let { error ->
                                 showError(error)
                             }
                         }
                         is Resource.Loading -> {
                             showProgess()
                         }
                     }
                 }

             }
            else
            {

            }


            binding.productlayout.visibility = View.GONE
        }
    }


    private fun productDetailVM() {
        productVM.productDetailVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1 && response.data != null) {
                            binding.productName.text=response.data.title.toString()
                            binding.quantityValue.text=response.data.price.toString()
                            Glide.with(requireContext()).load(response.data.additionalImage[0]).into(binding.productImage)

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

    override fun viewModelObserver() {
        chattingVM.getChat(chatId)
        chattingVM.chatsLiveData.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    if (response.data?.data != null) {
                        response.data.data!!.messages.reverse()
                        chatListAdapter.addData(response.data.data!!.messages)
                        binding.rvChattingList.scrollToPosition(response.data.data!!.messages.size-1)
                    } else {
                        dataNotFound()
                    }
                }

                is Resource.Error -> {
                    //  Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                    response.errorCode?.let { error ->
                        showError(error)
                    }
                }
                is Resource.Loading -> {
                    showProgess()
                }
            }

        }
    }

    override fun setup() {
        verticalAdapter()
    }

    private fun verticalAdapter() {
        chatListAdapter = ChattingListAdatper(favProductList,viewModel)
        binding.rvChattingList.addLinearAdapter(activity,  chatListAdapter)
    }

    override fun viewClick() {
        binding.messageEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                messageString = s.toString().trim()
            }
        })
        binding.sendMsg.setOnClickListener(View.OnClickListener {
            if (messageString != ""){
              chattingVM.sendMessage(messageString, chatId,0)
            }
            chattingVM.sendMessageLiveData.observe(this) { response ->
                when (response) {
                    is Resource.Success -> {
                        showContent()
                        if (response.data?.status == 1) {
                            chattingVM.getChat(chatId)
                            chatListAdapter.notifyDataSetChanged()
                            binding.messageEt.setText("")
                        } else {
                            dataNotFound()
                        }
                    }

                    is Resource.Error -> {
                        //  Utils.showBtn(binding.reset.progress, binding.reset.commonBtn)
                        response.errorCode?.let { error ->
                            showError(error)
                        }
                    }
                    is Resource.Loading -> {
                        showProgess()
                    }
                }
            }
        })
    }

    override fun onRetrybtn() {

    }
}