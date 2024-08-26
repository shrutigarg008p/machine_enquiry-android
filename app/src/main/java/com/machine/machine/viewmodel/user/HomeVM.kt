package com.machine.machine.viewmodel.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.GetCustomerMsgCountResponse
import com.machine.machine.model.response.user.HomeResponse
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch


class HomeVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val homeData: MutableLiveData<Resource<HomeResponse>> = MutableLiveData()
    val customerMsgCountData:MutableLiveData<Resource<GetCustomerMsgCountResponse>> = MutableLiveData()


    fun getHomeData() {
        viewModelScope.launch {
            homeData.postValue(Resource.Loading())

            try {
                val response = appRepository.getHomeData()
                homeData.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                homeData.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }

    fun getCustomerChatData() {
        viewModelScope.launch {
            customerMsgCountData.postValue(Resource.Loading())

            try {
                val response = appRepository.customerChatCount()
                customerMsgCountData.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                customerMsgCountData.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }

}