package com.machine.machine.viewmodel.user

import AddChatResponse
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.DistanceMatrixResponse
import com.machine.machine.model.response.user.AddFavouriteResponse
import com.machine.machine.model.response.user.ShopDetailResponse
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch


class ShopDetailVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val shopDetailVM: MutableLiveData<Resource<ShopDetailResponse>> = MutableLiveData()
    val distanceMatrixVM : MutableLiveData<Resource<DistanceMatrixResponse>> = MutableLiveData()
    val addFavouriteShopVM: MutableLiveData<Resource<AddFavouriteResponse>> = MutableLiveData()
    val createChatList: MutableLiveData<Resource<AddChatResponse>> = MutableLiveData()

    fun addFavouriteShop(category: Long) {
        viewModelScope.launch {
            addFavouriteShopVM.postValue(Resource.Loading())

            try {
                val response = appRepository.addShopToFavourite(category)
                addFavouriteShopVM.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                addFavouriteShopVM.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun addChatRequest(sellerId: List<Int>) {
        viewModelScope.launch {
            createChatList.postValue(Resource.Loading())

            try {
                val response = appRepository.addChatRequest(sellerId)
                createChatList.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                addFavouriteShopVM.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun getShopDetail(shopId: Long) {
        viewModelScope.launch {
            shopDetailVM.postValue(Resource.Loading())

            try {
                val response = appRepository.getShopDetail(shopId)
                shopDetailVM.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                shopDetailVM.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }

    fun distanceMatrixVM(origins:String,destinations:String,mode:String,language: String,key:String) {
        viewModelScope.launch {
            distanceMatrixVM.postValue(Resource.Loading())

            try {
                val response = appRepository.distanceMatrixGoogleApi(origins,destinations,mode,language,key)
                distanceMatrixVM.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

//                distanceMatrixVM.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }

}