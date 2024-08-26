package com.machine.machine.ui.user.screen.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.machine.machine.adapter.user.chat.chatListAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUChatBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.chat.ChatList
import com.machine.machine.util.addGridVerticalAdapter
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ChattingVM

class UChatFragment : BaseFragramentLoaderVM<FragmentUChatBinding>() {
    private lateinit var chattingVM: ChattingVM
    private lateinit var chatListAdapter: chatListAdapter
    private var favProductList: ArrayList<ChatList> = ArrayList()


    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUChatBinding.inflate(inflater, container, false)


    override fun appHeader() {

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        chattingVM = ViewModelProvider(this, factory)[ChattingVM::class.java]
    }

    override fun viewModelObserver() {
        chattingVM.getChatListApi()
        chattingVM.changePasswordLiveData.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    if (response.data?.data != null && response.data.data!!.data.size>0) {
                        chatListAdapter.addData(response.data.data!!.data)
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
        chatListAdapter = chatListAdapter(favProductList)
        binding.rvChattingList.addGridVerticalAdapter(activity, 1, chatListAdapter)
    }

    override fun viewClick() {

    }

    override fun onRetrybtn() {
    }


}