package com.machine.machine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.CommonResponse
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.LoginResponse
import com.machine.machine.network.RequestBodies
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.Event
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch

class OTPSendVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val _newAccountResp = MutableLiveData<Event<Resource<CommonResponse>>>()
    val accountResp: LiveData<Event<Resource<CommonResponse>>> = _newAccountResp

    private val _otpVerifyResp = MutableLiveData<Event<Resource<LoginResponse>>>()
    val otpVerifyResp: LiveData<Event<Resource<LoginResponse>>> = _otpVerifyResp


    fun OtpSend(body: RequestBodies.RegisterOtp) {
        viewModelScope.launch {
            _newAccountResp.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.registerOTP(body)
                _newAccountResp.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response,
                            getApplication<MyApplication>()
                        )
                    )
                )
            } catch (t: Throwable) {
                _newAccountResp.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }


    }

    fun verifyOtp(body: RequestBodies.OtpVerfiy) {
        viewModelScope.launch {
            _otpVerifyResp.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.otpVerify(body)
                _otpVerifyResp.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response,
                            getApplication<MyApplication>()
                        )
                    )
                )
            } catch (t: Throwable) {
                _otpVerifyResp.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }


    }
}