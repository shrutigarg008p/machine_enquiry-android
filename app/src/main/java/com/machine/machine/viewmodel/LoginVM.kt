package com.machine.machine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.LoginResponse
import com.machine.machine.network.RequestBodies
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.Event
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch

class LoginVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val _loginResponse = MutableLiveData<Event<Resource<LoginResponse>>>()
    val loginResponse: LiveData<Event<Resource<LoginResponse>>> = _loginResponse

    fun loginUser(body: RequestBodies.LoginBody) = viewModelScope.launch {
        login(body)
    }

    private suspend fun login(body: RequestBodies.LoginBody) {

        _loginResponse.postValue(Event(Resource.Loading()))
        try {
            val response = appRepository.loginUser(body)
            _loginResponse.postValue(
                Event(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            )

        } catch (t: Throwable) {
            _loginResponse.postValue(
                Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
            )

        }
    }

}