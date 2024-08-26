package com.machine.machine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.CommonResponse
import com.machine.machine.model.common.Resource
import com.machine.machine.network.RequestBodies
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.Event
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch


class ForgetVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val _forgetResponse = MutableLiveData<Event<Resource<CommonResponse>>>()
    val forgetLiveData: LiveData<Event<Resource<CommonResponse>>> = _forgetResponse


    fun forgetApi(
        body: RequestBodies.forgetPassword
    ) {
        viewModelScope.launch {
            _forgetResponse.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.forgetPassword(body)
                _forgetResponse.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response,
                            getApplication<MyApplication>()
                        )
                    )
                )

            } catch (t: Throwable) {
                _forgetResponse.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }
    }

}