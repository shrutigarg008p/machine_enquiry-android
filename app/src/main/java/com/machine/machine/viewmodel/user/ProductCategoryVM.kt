package com.machine.machine.viewmodel.user

import AddChatResponse
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.CategoryResponse
import com.machine.machine.model.response.user.AddFavouriteResponse
import com.machine.machine.model.response.user.AddRemoveCartResponse
import com.machine.machine.model.response.user.ProductDetailResponse
import com.machine.machine.model.response.user.ProductListResponse
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ProductCategoryVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val productDetailVM: MutableLiveData<Resource<ProductDetailResponse>> = MutableLiveData()
    val productListVM: MutableLiveData<Resource<ProductListResponse>> = MutableLiveData()
    val subCategoryListVM: MutableLiveData<Resource<CategoryResponse>> = MutableLiveData()

    val addFavouriteProductVM: MutableLiveData<Resource<AddFavouriteResponse>> = MutableLiveData()
    val addCartItemLD: MutableLiveData<Resource<AddRemoveCartResponse>> = MutableLiveData()

    val createChatList: MutableLiveData<Resource<AddChatResponse>> = MutableLiveData()
    fun subcategoryListAPi(category: Long) {
        viewModelScope.launch {
            subCategoryListVM.postValue(Resource.Loading())
            try {
                val response = appRepository.subCategoryList(category)
                subCategoryListVM.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )

            } catch (t: Throwable) {
                subCategoryListVM.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun getProductListById(page: Int, subCategoryId: Long, shopId: Long) {
        viewModelScope.launch {
            productListVM.postValue(Resource.Loading())

            try {
                 val response = appRepository.getProductListByCatID(page, subCategoryId, shopId)
                productListVM.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                productListVM.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun addFavouriteProduct(productId: Long?) {
        viewModelScope.launch {
            addFavouriteProductVM.postValue(Resource.Loading())

            try {
                val response = appRepository.addProductToFavourite(productId!!)
                addFavouriteProductVM.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                addFavouriteProductVM.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun getProductDetailApi(productId: Long) {
        viewModelScope.launch {
            productDetailVM.postValue(Resource.Loading())

            try {
                val response = appRepository.getProductDetail(productId)
                productDetailVM.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                productDetailVM.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

    fun addCardItem(productId: Long, quantity: Long) {
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

                addFavouriteProductVM.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }


    fun addChatmewRequest(sellerId: List<Int>): String {
       var channleid="";

                val response = appRepository.addChatnewRequest(sellerId)
                channleid= response.body()?.data?.channelId.toString();
                Log.i("new_channel_id",channleid)

         return channleid
    }


}