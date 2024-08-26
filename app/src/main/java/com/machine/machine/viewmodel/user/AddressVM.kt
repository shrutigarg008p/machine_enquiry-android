package com.machine.machine.viewmodel.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.CommonResponse
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.AddressListResponse
import com.machine.machine.model.response.user.AddressObjResponse
import com.machine.machine.network.RequestBodies
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddressVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val addressListMLD: MutableLiveData<Resource<AddressListResponse>> = MutableLiveData()
    val primaryAddressMLD: MutableLiveData<Resource<CommonResponse>> = MutableLiveData()
    val deleteAddressMLD: MutableLiveData<Resource<CommonResponse>> = MutableLiveData()
    val addressByIdMLD: MutableLiveData<Resource<AddressObjResponse>> = MutableLiveData()
    val addAddressMLD: MutableLiveData<Resource<CommonResponse>> = MutableLiveData()
    val updateAddressMLD: MutableLiveData<Resource<CommonResponse>> = MutableLiveData()


    fun getAllAddressListApi() {
        viewModelScope.launch {
            addressListMLD.postValue(Resource.Loading())

            try {
                val response = appRepository.getAllAddressList()
                addressListMLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                addressListMLD.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }

    fun setPrimaryAddress(addressId: Long) {
        viewModelScope.launch {
            primaryAddressMLD.postValue(Resource.Loading())

            try {
                val response = appRepository.setAsAddressPrimary(addressId, 1)
                primaryAddressMLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                primaryAddressMLD.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun deleteAddress(addressId: Long) {
        viewModelScope.launch {
            deleteAddressMLD.postValue(Resource.Loading())

            try {
                val response = appRepository.deleteAddress(addressId)
                deleteAddressMLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                deleteAddressMLD.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }


    fun getAddressById(addressId: Long) {
        viewModelScope.launch {
            addressByIdMLD.postValue(Resource.Loading())

            try {
                val response = appRepository.getAddressById(addressId)
                addressByIdMLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                addressByIdMLD.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }


    fun addAddress(body: RequestBodies.Address) {
        viewModelScope.launch {
            addAddressMLD.postValue(Resource.Loading())

            try {
                val response = appRepository.addAddress(getData(body))
                addAddressMLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                addAddressMLD.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun updateAddress(body: RequestBodies.Address) {
        viewModelScope.launch {
            updateAddressMLD.postValue(Resource.Loading())

            try {
                val response = appRepository.updateAddress(getData(body))
                updateAddressMLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                updateAddressMLD.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }


    private fun getData(body: RequestBodies.Address): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("name", body.name)
            .addFormDataPart("email", body.email)
            .addFormDataPart("phone", body.phone)
            .addFormDataPart("address_1", body.address1)
            .addFormDataPart("address_2", body.address2)
            .addFormDataPart("city", body.city)
            .addFormDataPart("zip", body.zip)
            .addFormDataPart("state", body.state)
            .addFormDataPart("country", body.country)
            .addFormDataPart("latitude", body.latitude)
            .addFormDataPart("longitude", body.longitude)
        val requestBody: RequestBody = builder.build()
        return requestBody
    }
}