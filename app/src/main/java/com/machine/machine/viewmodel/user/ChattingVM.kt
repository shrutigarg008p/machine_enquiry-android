package com.machine.machine.viewmodel.user

import ChatResponseModel1
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.CommonResponse
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.chat.ChatResponseModel
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch

class ChattingVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {


//    val chatListObserver = MutableLiveData<Resource<ChatResponseModel>>()

    private val chatListObserver = MutableLiveData<Resource<ChatResponseModel>>()
    val changePasswordLiveData: LiveData<Resource<ChatResponseModel>> = chatListObserver

    private val chatsObserver = MutableLiveData<Resource<ChatResponseModel1>>()
    val chatsLiveData: LiveData<Resource<ChatResponseModel1>> = chatsObserver

    private val sendMessageObserver = MutableLiveData<Resource<CommonResponse>>()
    val sendMessageLiveData: LiveData<Resource<CommonResponse>> = sendMessageObserver


    fun getChatListApi(
    ) {
        viewModelScope.launch {
            chatListObserver.postValue(Resource.Loading())
            try {
                val response = appRepository.getChatList()
                chatListObserver.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {
                chatListObserver.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun getChat(id: Int) {
        viewModelScope.launch {
            chatsObserver.postValue(Resource.Loading())
            try {
                val response = appRepository.getChats(id)
                chatsObserver.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {
                chatsObserver.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun sendMessage(
        message: String,
        id: Int,
        type:Int
    ) {
        viewModelScope.launch {
            sendMessageObserver.postValue(Resource.Loading())
            try {
                val response = appRepository.sendMessage(message, id,type)
                sendMessageObserver.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {
                sendMessageObserver.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }


}