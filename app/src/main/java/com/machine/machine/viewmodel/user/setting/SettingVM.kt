package com.machine.machine.viewmodel.user.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.CommonResponse
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.SettingResponse
import com.machine.machine.model.response.user.UserNotificationResponse
import com.machine.machine.network.RequestBodies
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.Event
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


class SettingVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val _editProfile = MutableLiveData<Event<Resource<CommonResponse>>>()
    val editProfileLiveData: LiveData<Event<Resource<CommonResponse>>> = _editProfile

    private val _changePassword = MutableLiveData<Event<Resource<CommonResponse>>>()
    val changePasswordLiveData: LiveData<Event<Resource<CommonResponse>>> = _changePassword

    private val _helpSupport = MutableLiveData<Event<Resource<CommonResponse>>>()
    val helpSupportLiveData: LiveData<Event<Resource<CommonResponse>>> = _helpSupport


    private val _notificationVM = MutableLiveData<Event<Resource<CommonResponse>>>()
    val notificationVM: LiveData<Event<Resource<CommonResponse>>> = _notificationVM

    private val _userNotificationVM = MutableLiveData<Resource<UserNotificationResponse>>()
    val userNotificationVM: LiveData<Resource<UserNotificationResponse>> = _userNotificationVM

    private val _userNotificationReadVM = MutableLiveData<Resource<UserNotificationResponse>>()
    val userNotificationReadVM: LiveData<Resource<UserNotificationResponse>> = _userNotificationReadVM

    private val _sellerNotificationVM = MutableLiveData<Resource<UserNotificationResponse>>()
    val sellerNotificationVM: LiveData<Resource<UserNotificationResponse>> = _sellerNotificationVM

    private val _sellerNotificationReadVM = MutableLiveData<Resource<UserNotificationResponse>>()
    val sellerNotificationReadVM: LiveData<Resource<UserNotificationResponse>> = _sellerNotificationReadVM


    private val _LogoutVM = MutableLiveData<Event<Resource<CommonResponse>>>()
    val logoutVM: LiveData<Event<Resource<CommonResponse>>> = _LogoutVM

    val settingObserver = MutableLiveData<Resource<SettingResponse>>()

    fun getSettingApi(
    ) {
        viewModelScope.launch {
            settingObserver.postValue(Resource.Loading())
            try {
                val response = appRepository.getSettingData()
                settingObserver.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {
                settingObserver.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun changePasswordApi(
        body: RequestBodies.changePassword
    ) {
        viewModelScope.launch {
            _changePassword.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.changePassword(body)
                _changePassword.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response,
                            getApplication<MyApplication>()
                        )
                    )
                )

            } catch (t: Throwable) {
                _changePassword.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }
    }

    fun editProfile(
        body: RequestBodies.editProfile
    ) {
        viewModelScope.launch {
            _editProfile.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.editProfile(getData(body))
                _editProfile.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response,
                            getApplication<MyApplication>()
                        )
                    )
                )

            } catch (t: Throwable) {
                _editProfile.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }
    }


    fun helpAndSupportApi(
        body: RequestBodies.helpAndSupport
    ) {
        viewModelScope.launch {
            _helpSupport.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.helpAndSupport(body)
                _helpSupport.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response,
                            getApplication<MyApplication>()
                        )
                    )
                )

            } catch (t: Throwable) {
                _helpSupport.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }
    }


    fun notificationApi(
        notification: String
    ) {
        viewModelScope.launch {
            _notificationVM.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.notification(notification)
                _notificationVM.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response,
                            getApplication<MyApplication>()
                        )
                    )
                )

            } catch (t: Throwable) {
                _notificationVM.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }
    }

    fun logoutApi() {
        viewModelScope.launch {
            _LogoutVM.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.logout()
                _LogoutVM.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response,
                            getApplication<MyApplication>()
                        )
                    )
                )

            } catch (t: Throwable) {
                _LogoutVM.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }
    }

    fun getCustomerNotificationApi(
    ) {
        viewModelScope.launch {
            _userNotificationVM.postValue(Resource.Loading())
            try {
                val response = appRepository.customerNotification()
                _userNotificationVM.postValue(

                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )

                )

            } catch (t: Throwable) {
                _userNotificationVM.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun getCustomerNotificationReadApi(
    ) {
        viewModelScope.launch {
            _userNotificationReadVM.postValue(Resource.Loading())
            try {
                val response = appRepository.customerNotificationRead()
                _userNotificationReadVM.postValue(

                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )

                )
            } catch (t: Throwable) {
                _userNotificationReadVM.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun getSellerNotificationApi(
    ) {
        viewModelScope.launch {
            _sellerNotificationVM.postValue(Resource.Loading())
            try {
                val response = appRepository.sellerNotification()
                _sellerNotificationVM.postValue(

                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )

                )

            } catch (t: Throwable) {
                _sellerNotificationVM.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun getSellerNotificationReadApi(
    ) {
        viewModelScope.launch {
            _sellerNotificationReadVM.postValue(Resource.Loading())
            try {
                val response = appRepository.sellerNotificationRead()
                _sellerNotificationReadVM.postValue(

                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )

                )
            } catch (t: Throwable) {
                _sellerNotificationReadVM.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    private fun getData(body: RequestBodies.editProfile): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val images = body.profile_pic
        if (images.name!=""){
            builder.addFormDataPart("name", body.name)
                   .addFormDataPart("profile_pic", images.name,
                    RequestBody.create(MultipartBody.FORM, images))
        }else{
            builder.addFormDataPart("name", body.name)
        }

        val requestBody: RequestBody = builder.build()
        return requestBody


    }
}