package com.machine.machine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.example.ShopListSeller
import com.machine.machine.Quatation
import com.machine.machine.QuatationDetails
import com.machine.machine.model.CommonResponse
import com.machine.machine.model.PicsResponse
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.GetSellerMsgCountResponse
import com.machine.machine.model.response.seller.AllProducts
import com.machine.machine.repository.AppRepository
import com.machine.machine.ui.MyApplication
import com.machine.machine.util.ResUtil
import kotlinx.coroutines.launch

class PicsViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val picsData: MutableLiveData<Resource<PicsResponse>> = MutableLiveData()
    val shopsData: MutableLiveData<Resource<ShopListSeller>> = MutableLiveData()
    val productsData: MutableLiveData<Resource<AllProducts>> = MutableLiveData()
    val updateProduct: MutableLiveData<Resource<CommonResponse>> = MutableLiveData()
    val QutationData: MutableLiveData<Resource<Quatation>> = MutableLiveData()
    val QutationDetailsData: MutableLiveData<Resource<QuatationDetails>> = MutableLiveData()
    val QutationDetailsForCustomerData: MutableLiveData<Resource<QuatationDetails>> = MutableLiveData()
    val sellerChatCount :MutableLiveData<Resource<GetSellerMsgCountResponse>> = MutableLiveData()

    init {
        getSellerChatAndRfqCountAndRData()
        getSellerShops()
        getPictures()
        getRFQDATA()
    //  getrfqdetails(orde)
    }

    fun getPictures() = viewModelScope.launch {
        fetchPics()
    }

    fun getSellerShops() = viewModelScope.launch {
        shopsData()
    }

    fun getRFQDATA() = viewModelScope.launch {
        rfqData()
    }

    fun getrfqdetails(orderno: String)=viewModelScope.launch{

        rfqDetailsData(orderno)
    }

    fun getrfqdetailsForCustomer(orderno: String)=viewModelScope.launch{

        rfqDetailForCustomerData(orderno)
    }

    fun getAllProduct(shopId: Int, subProductId: Int, myProductOnly: Int, page: Int) =
        viewModelScope.launch {
            productsApi(shopId, subProductId, myProductOnly, page)
        }

    fun getAllProduct(shopId: Int,page: Int,myProductOnly:Int) =
        viewModelScope.launch {
            productsApi(shopId,page,myProductOnly)
        }

    fun updateProduct(productId: Int, qty: String, priceType: String, price: String, shopId: Int) =
        viewModelScope.launch {
            updateproductsApi(productId, qty, priceType, price, shopId)
        }


    fun removeProduct(productId: Int, shopId: Int) =
        viewModelScope.launch {
            removeProductApi(productId, shopId)
        }

    fun bidingProduct(productId: Int, shopId: Int) =
        viewModelScope.launch {
            bidingProductApi(productId, shopId)
        }

    fun acceptRejectQuotationProduct(oderId: String, accepted: Int) =
        viewModelScope.launch {
            acceptRejectQuotationApi(oderId, accepted)
        }

    private suspend fun productsApi(
        shopId: Int,
        subProductId: Int,
        myProductOnly: Int,
        page: Int
    ) {
        viewModelScope.launch {
            productsData.postValue(Resource.Loading())
            try {
                val response =
                    appRepository.getAllProducts(shopId, subProductId, myProductOnly, page)

                productsData.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )

            } catch (t: Throwable) {
                productsData.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    private suspend fun productsApi(shopId:Int,page: Int,myProductOnly:Int) {
        viewModelScope.launch {
            productsData.postValue(Resource.Loading())
            try {
                val response =
                    appRepository.getAllProducts(shopId,page,myProductOnly)

                productsData.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )

            } catch (t: Throwable) {
                productsData.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    private suspend fun updateproductsApi(
        productId: Int, qty: String, priceType: String, price: String, shopId: Int
    ) {
        viewModelScope.launch {
            updateProduct.postValue(Resource.Loading())
            try {
                val response =
                    appRepository.updateProducts(productId, qty, priceType, price, shopId)

                updateProduct.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )



            } catch (t: Throwable) {
                productsData.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }


    private suspend fun removeProductApi(
        productId: Int, shopId: Int
    ) {
        viewModelScope.launch {
            updateProduct.postValue(Resource.Loading())
            try {
                val response =
                    appRepository.removeProducts(productId, shopId)

                updateProduct.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )



            } catch (t: Throwable) {
                productsData.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }


    private suspend fun bidingProductApi(
        productId: Int, shopId: Int
    ) {
        viewModelScope.launch {
            updateProduct.postValue(Resource.Loading())
            try {
                val response =
                    appRepository.bidUpdate(productId, shopId)

                updateProduct.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )



            } catch (t: Throwable) {
                productsData.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }

    private suspend fun acceptRejectQuotationApi(
        oderId: String, accepted: Int
    ) {
        viewModelScope.launch {
            updateProduct.postValue(Resource.Loading())
            try {
                val response =
                    appRepository.acceptRejectQuotation(oderId, accepted)

                updateProduct.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )



            } catch (t: Throwable) {
                productsData.postValue(
                    ResUtil.getErrorEvent(t, getApplication<MyApplication>())
                )

            }
        }
    }


    private suspend fun fetchPics() {
        picsData.postValue(Resource.Loading())

        try {
            val response = appRepository.getPictures()
            picsData.postValue(
                ResUtil.handleResponse(
                    response,
                    getApplication<MyApplication>()
                )
            )
        } catch (t: Throwable) {

            picsData.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

        }
    }


    private suspend fun shopsData() {
        shopsData.postValue(Resource.Loading())

        try {
            val response = appRepository.Sellershop()
            shopsData.postValue(
                ResUtil.handleResponse(
                    response,
                    getApplication<MyApplication>()
                )
            )
        } catch (t: Throwable) {

            shopsData.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

        }
    }


    private suspend fun rfqData() {
        QutationData.postValue(Resource.Loading())

        try {
            val response = appRepository.rfq()
            QutationData.postValue(
                ResUtil.handleResponse(
                    response,
                    getApplication<MyApplication>()
                )
            )
        } catch (t: Throwable) {

            QutationData.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

        }
    }


    private suspend fun rfqDetailsData(orderno:String) {
        QutationDetailsData.postValue(Resource.Loading())

        try {
            val response = appRepository.rfqDetails(orderno)
            QutationDetailsData.postValue(
                ResUtil.handleResponse(
                    response,
                    getApplication<MyApplication>()
                )
            )
        } catch (t: Throwable) {

            QutationDetailsData.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

        }
    }

    private suspend fun rfqDetailForCustomerData(orderno:String) {
        QutationDetailsForCustomerData.postValue(Resource.Loading())

        try {
            val response = appRepository.rfqDeatilsForCustomerData(orderno)
            QutationDetailsForCustomerData.postValue(
                ResUtil.handleResponse(
                    response,
                    getApplication<MyApplication>()
                )
            )
        } catch (t: Throwable) {

            QutationDetailsForCustomerData.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

        }
    }

    fun getSellerChatAndRfqCountAndRData() {
        viewModelScope.launch {
            sellerChatCount.postValue(Resource.Loading())

            try {
                val response = appRepository.sellerChatCount()
                sellerChatCount.postValue(
                    ResUtil.handleResponse(
                        response, getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                sellerChatCount.postValue(ResUtil.getErrorEvent(t, getApplication<MyApplication>()))

            }
        }
    }
}