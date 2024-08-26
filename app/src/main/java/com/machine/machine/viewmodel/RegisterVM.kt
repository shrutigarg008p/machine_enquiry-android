package com.machine.machine.viewmodel

import CreateShopResponse
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.model.common.Resource
import com.machine.machine.model.requestBodies.CreateNewShopBody
import com.machine.machine.model.requestBodies.RegisterBody
import com.machine.machine.model.response.CategoryResponse
import com.machine.machine.model.response.RegistrationResponse
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.Event
import com.machine.machine.util.ResUtil
import com.machine.machine.util.removeWhitespaces
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


class RegisterVM(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val _registerLd = MutableLiveData<Event<Resource<RegistrationResponse>>>()
    val registerLD: LiveData<Event<Resource<RegistrationResponse>>> = _registerLd

    val categoryList: MutableLiveData<Resource<CategoryResponse>> = MutableLiveData()
    val createNewShopResponse: MutableLiveData<Resource<CreateShopResponse>> = MutableLiveData()

    fun categoryListAPi() {
        viewModelScope.launch {
            categoryList.postValue(Resource.Loading())
            try {
                val response = appRepository.categoryList()
                categoryList.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )

            } catch (t: Throwable) {
                categoryList.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    fun registerAPi(
        body: RegisterBody, userType: String
    ) {
        viewModelScope.launch {
            _registerLd.postValue(Event(Resource.Loading()))
            try {
                val response = appRepository.register(getData(body, userType))
                _registerLd.postValue(
                    Event(
                        ResUtil.handleResponse(
                            response, getApplication<MyApplication>()
                        )
                    )
                )

            } catch (t: Throwable) {
                _registerLd.postValue(
                    Event(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))
                )

            }
        }
    }


    // create new Shop

    fun createNewShop(
        body: CreateNewShopBody
    ) {
        viewModelScope.launch {
            createNewShopResponse.postValue(Resource.Loading())
            try {
                val response = appRepository.createNewShop(getDataForCreateNewShop(body))
                createNewShopResponse.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )

            } catch (t: Throwable) {
                categoryList.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    private fun getData(body: RegisterBody, type: String): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (type == IDConst.CUSTOMER) {

            builder.addFormDataPart("name", body.name ?: BaseConstants.EMPTY)
                .addFormDataPart("email", body.email ?: BaseConstants.EMPTY)
                .addFormDataPart("phone", body.phone ?: BaseConstants.EMPTY)
                .addFormDataPart("password", body.password ?: BaseConstants.EMPTY)
                .addFormDataPart(
                    "password_confirmation",
                    body.password_confirmation ?: BaseConstants.EMPTY
                )

        } else {
            builder.addFormDataPart("name", body.name ?: BaseConstants.EMPTY)
                .addFormDataPart("email", body.email ?: BaseConstants.EMPTY)
                .addFormDataPart("phone", body.phone ?: BaseConstants.EMPTY)
                .addFormDataPart("password", body.password ?: BaseConstants.EMPTY)
                .addFormDataPart(
                    "password_confirmation",
                    body.password_confirmation ?: BaseConstants.EMPTY
                )
                .addFormDataPart("shop_name", body.shop_name ?: BaseConstants.EMPTY)
                .addFormDataPart("shop_owner", body.shopOwnerName ?: BaseConstants.EMPTY)
                .addFormDataPart("shop_email", body.shop_email ?: BaseConstants.EMPTY)
                .addFormDataPart("shop_number", body.shop_number ?: BaseConstants.EMPTY)
                .addFormDataPart("registration_no", body.registration_no ?: BaseConstants.EMPTY)
                .addFormDataPart("country", body.country ?: BaseConstants.EMPTY)
                .addFormDataPart("state", body.state ?: BaseConstants.EMPTY)
                .addFormDataPart("address_1", body.address_1 ?: BaseConstants.EMPTY)
                .addFormDataPart("address_2", body.address_2 ?: BaseConstants.EMPTY)
                .addFormDataPart(
                    "working_hours_from",
                    removedSpace(body.working_hours_from)
                )
                .addFormDataPart(
                    "working_hours_to",
                    removedSpace(body.working_hours_to)
                )
                .addFormDataPart("latitude", body.latitude ?: BaseConstants.EMPTY)
                .addFormDataPart("longitude", body.longitude ?: BaseConstants.EMPTY)

            for (cat in body.category) {
                builder.addFormDataPart("product_categories[]",cat.toString())
            }


            // days
            for (days in body.workingDayList) {
                builder.addFormDataPart("working_days[]", days)
            }

            // Images
            // Images
            for (PicItem in body.shop_imagePath) {
                if (PicItem.pic_path != null) {
                    val images = PicItem.pic_path
                    if (images?.exists() == true) {
                        builder.addFormDataPart(
                            "photos[]",
                            images.getName(),
                            RequestBody.create(MultipartBody.FORM, images)
                        )
                    }
                }
            }
        }

        val requestBody: RequestBody = builder.build()

        return requestBody

    }


    private fun getDataForCreateNewShop(body: CreateNewShopBody): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)


        builder.addFormDataPart("shop_owner", body.shopOwner ?: BaseConstants.EMPTY)
            .addFormDataPart("shop_name", body.shopName ?: BaseConstants.EMPTY)
            .addFormDataPart("shop_email", body.shopEmail ?: BaseConstants.EMPTY)
            .addFormDataPart("shop_contact", body.shopContact ?: BaseConstants.EMPTY)
            .addFormDataPart("registration_no", body.registrationNo ?: BaseConstants.EMPTY)
            .addFormDataPart("country", body.country ?: BaseConstants.EMPTY)
            .addFormDataPart("state", body.state ?: BaseConstants.EMPTY)
            .addFormDataPart("address_1", body.address_1 ?: BaseConstants.EMPTY)
            .addFormDataPart("address_2", body.address_2 ?: BaseConstants.EMPTY)
            .addFormDataPart("working_hours_from", removedSpace(body.workingHoursFrom))
            .addFormDataPart("working_hours_to", removedSpace(body.workingHoursTo))
            .addFormDataPart("latitude", body.latitude ?: BaseConstants.EMPTY)
            .addFormDataPart("longitude", body.longitude ?: BaseConstants.EMPTY)

        for (categories in body.productCategories) {
            builder.addFormDataPart("product_categories[]", categories.toString())
        }
        for (days in body.workingDays) {
            builder.addFormDataPart("working_days[]", days)
        }


        for (PicItem in body.photos) {
            if (PicItem.pic_path != null) {
                val images = PicItem.pic_path
                if (images?.exists() == true) {
                    builder.addFormDataPart(
                        "photos[]",
                        images.getName(),
                        RequestBody.create(MultipartBody.FORM, images)
                    )
                }
            }
        }

        val requestBody: RequestBody = builder.build()

        return requestBody

    }


    private fun removedSpace(value: String?): String {
        if (value != null) {
            return value.removeWhitespaces()
        }
        return BaseConstants.EMPTY

    }


}

