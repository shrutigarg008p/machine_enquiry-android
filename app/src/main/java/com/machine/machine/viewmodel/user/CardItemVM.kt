package com.machine.machine.viewmodel.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.CommonResponse
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.AddRemoveCartResponse
import com.machine.machine.model.response.user.CartListingResponse
import com.machine.machine.network.RequestBodies
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch


class CardItemVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val cartItemListLD: MutableLiveData<Resource<CartListingResponse>> = MutableLiveData()
    val removeCartItemLD: MutableLiveData<Resource<AddRemoveCartResponse>> = MutableLiveData()
    val addCartItemLD: MutableLiveData<Resource<AddRemoveCartResponse>> = MutableLiveData()
    val submitQuotation: MutableLiveData<Resource<CommonResponse>> = MutableLiveData()


    fun getAllCartItemApi() {
        viewModelScope.launch {
            cartItemListLD.postValue(Resource.Loading())

            try {
                val response = appRepository.getAllCartItem()
                cartItemListLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                cartItemListLD.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }

    fun addCardItemApi(productId: Long, quantity: Long) {
        viewModelScope.launch {
            addCartItemLD.postValue(Resource.Loading())

            try {
                val response = appRepository.addCardItem(productId, quantity)
                addCartItemLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {
                addCartItemLD.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
            }
        }
    }

    fun removeCardItemApi(productId: Long, quantity: Long) {
        viewModelScope.launch {
            removeCartItemLD.postValue(Resource.Loading())

            try {
                val response = appRepository.removeCardItem(productId, quantity)
                removeCartItemLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                removeCartItemLD.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun callSendQuotationApi(
        addressId: String,
        deliveryType: String,
        list: ArrayList<RequestBodies.Items>
    ) {

        var requestBody = RequestBodies.submitQuotation()
        requestBody.address_id = addressId
        requestBody.delivery_type = deliveryType
        requestBody.items = list

        viewModelScope.launch {
            submitQuotation.postValue(Resource.Loading())

            try {
                val response = appRepository.sendQuotation(requestBody)
                submitQuotation.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                submitQuotation.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }


}