package com.machine.machine.viewmodel.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.AddFavouriteResponse
import com.machine.machine.model.response.user.FavouriteResponse
import com.machine.machine.model.response.user.ProductListResponse
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch


class FavouriteVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val shopFavLD: MutableLiveData<Resource<FavouriteResponse>> = MutableLiveData()
    val productLD: MutableLiveData<Resource<ProductListResponse>> = MutableLiveData()
    val removeFavShopLD: MutableLiveData<Resource<AddFavouriteResponse>> = MutableLiveData()
    val removedFavProductLd: MutableLiveData<Resource<AddFavouriteResponse>> = MutableLiveData()

    fun getShopFavouriteListApi() {
        viewModelScope.launch {
            shopFavLD.postValue(Resource.Loading())

            try {
                val response = appRepository.getShopFavList()
                shopFavLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                shopFavLD.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }

    fun getProductFavListApi() {
        viewModelScope.launch {
            productLD.postValue(Resource.Loading())

            try {
                val response = appRepository.getProductFavList()
                productLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                productLD.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }

    fun removedShopFavApi(category: Long) {
        viewModelScope.launch {
            removeFavShopLD.postValue(Resource.Loading())

            try {
                val response = appRepository.addShopToFavourite(category)
                removeFavShopLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                removeFavShopLD.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun removedFavProductApi(productId: Long) {
        viewModelScope.launch {
            removedFavProductLd.postValue(Resource.Loading())

            try {
                val response = appRepository.addProductToFavourite(productId)
                removedFavProductLd.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                removedFavProductLd.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

}