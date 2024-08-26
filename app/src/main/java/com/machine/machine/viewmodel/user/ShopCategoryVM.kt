package com.machine.machine.viewmodel.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.CategoryResponse
import com.machine.machine.model.response.user.AddFavouriteResponse
import com.machine.machine.model.response.user.ShopCatListIDResponse
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch


class ShopCategoryVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val shopCategoryListVM: MutableLiveData<Resource<ShopCatListIDResponse>> = MutableLiveData()

    val categoryListVM: MutableLiveData<Resource<CategoryResponse>> = MutableLiveData()
    val addFavouriteShopVM: MutableLiveData<Resource<AddFavouriteResponse>> = MutableLiveData()

    fun categoryListAPi() {
        viewModelScope.launch {
            categoryListVM.postValue(Resource.Loading())
            try {
                val response = appRepository.categoryList()
                categoryListVM.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )

            } catch (t: Throwable) {
                categoryListVM.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun getShopCategoryListById(page: Int, category: Long) {
        viewModelScope.launch {
            shopCategoryListVM.postValue(Resource.Loading())

            try {
                val response = appRepository.getCategoryShopListByID(page, category)
                shopCategoryListVM.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                shopCategoryListVM.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }

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

}