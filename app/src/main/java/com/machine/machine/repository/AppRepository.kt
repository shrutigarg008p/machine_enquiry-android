package com.machine.machine.repository


import android.view.textclassifier.TextLanguage
import com.machine.machine.network.RequestBodies
import com.machine.machine.network.RetrofitInstance
import okhttp3.RequestBody

public class AppRepository {


    /*Retrofit auth*/
    suspend fun loginUser(body: RequestBodies.LoginBody) =
        RetrofitInstance.authApi!!.loginUser(body.emailPhone, body.password,body.deviceToken)

    suspend fun registerOTP(body: RequestBodies.RegisterOtp) =
        RetrofitInstance.authApi!!.registerOTP(body.type, body.emailPhone)

    suspend fun otpVerify(body: RequestBodies.OtpVerfiy) =
        RetrofitInstance.authApi!!.otpVerify(body.otp, body.emailPhone)

    suspend fun forgetPassword(body: RequestBodies.forgetPassword) =
        RetrofitInstance.authApi!!.forgetPassword(
            body.otp,
            body.emailPhone,
            body.password,
            body.confirmPassword
        )


    /*Retrofit client*/
    suspend fun register(requestBody: RequestBody) =
        RetrofitInstance.ClientApi!!.register(requestBody)


    suspend fun categoryList() = RetrofitInstance.ClientApi!!.categoryListRegister(1)

    suspend fun subCategoryList(category: Long) =
        RetrofitInstance.ClientApi!!.subCategoryList(category)

    suspend fun editProfile(requestBody: RequestBody) =
        RetrofitInstance.ClientApi!!.editProfile(requestBody)

    suspend fun changePassword(body: RequestBodies.changePassword) =
        RetrofitInstance.ClientApi!!.changePassword(
            body.oldPassword,
            body.password,
            body.confirmPassword
        )

    suspend fun helpAndSupport(body: RequestBodies.helpAndSupport) =
        RetrofitInstance.ClientApi!!.helpAndSuppot(
            body.name,
            body.email,
            body.msg
        )

    suspend fun notification(notification: String) =
        RetrofitInstance.ClientApi!!.notification(notification)

    suspend fun getSettingData() = RetrofitInstance.ClientApi!!.getSettingData()

    suspend fun getChatList() = RetrofitInstance.ClientApi!!.getChatList()


    suspend fun getChats(id: Int) = RetrofitInstance.ClientApi!!.getChats(id)
    suspend fun sendMessage(message: String, id: Int,type:Int) =
        RetrofitInstance.ClientApi!!.sendMessage(message, id,type)


    suspend fun getHomeData() = RetrofitInstance.ClientApi!!.getHomeData()

    suspend fun getShopFavList() = RetrofitInstance.ClientApi!!.getShopFavList()

    suspend fun getProductFavList() = RetrofitInstance.ClientApi!!.getProductFavList()

    suspend fun getCategoryShopListByID(page: Int, category: Long) =
        RetrofitInstance.ClientApi!!.getCategoryShopListById(category)

    suspend fun addShopToFavourite(category: Long) =
        RetrofitInstance.ClientApi!!.addShopToFavourite(category)

    suspend fun getShopDetail(shopId: Long) =
        RetrofitInstance.ClientApi!!.getShopDetail(shopId)

    suspend fun addChatRequest(sellerId: List<Int>) =
        RetrofitInstance.ClientApi!!.getCreateChat(sellerId)

     fun addChatnewRequest(sellerId: List<Int>) =
        RetrofitInstance.ClientApi!!.getCreatenewChat(sellerId)


    suspend fun getProductListByCatID(page: Int, subCategoryId: Long, shopId: Long) =
        RetrofitInstance.ClientApi!!.getProductListById(page, subCategoryId, shopId)

    suspend fun addProductToFavourite(productId: Long) =
        RetrofitInstance.ClientApi!!.addProductToFavourite(productId)

    suspend fun getProductDetail(productId: Long) =
        RetrofitInstance.ClientApi!!.getProductDetail(productId)

    suspend fun logout() =
        RetrofitInstance.ClientApi!!.logout()


    /*cart api */

    suspend fun getAllCartItem() = RetrofitInstance.ClientApi!!.getAllCartItem()

    suspend fun addCardItem(productId: Long, quantity: Long) =
        RetrofitInstance.ClientApi!!.addCartItem(productId, quantity)

    suspend fun removeCardItem(productId: Long, quantity: Long) =
        RetrofitInstance.ClientApi!!.removeCardItem(productId, quantity)

    suspend fun sendQuotation(requestBody: RequestBodies.submitQuotation) =
        RetrofitInstance.ClientApi!!.sendQuotationsApi(requestBody)

    /*end*/
    suspend fun getAllAddressList() =
        RetrofitInstance.ClientApi!!.getAllAddress()

    suspend fun setAsAddressPrimary(addressId: Long, isPrimary: Long) =
        RetrofitInstance.ClientApi!!.setAsPrimaryAddress(addressId, isPrimary)

    suspend fun deleteAddress(addressId: Long) =
        RetrofitInstance.ClientApi!!.deleteAddress(addressId)


    suspend fun getAddressById(addressId: Long) =
        RetrofitInstance.ClientApi!!.getAddressById(addressId)

    suspend fun addAddress(requestBody: RequestBody) =
        RetrofitInstance.ClientApi!!.addAddress(requestBody)


    suspend fun updateAddress(requestBody: RequestBody) =
        RetrofitInstance.ClientApi!!.updateAddress(requestBody)

    suspend fun Sellershop() =
        RetrofitInstance.ClientApi!!.sellerShops()

    suspend fun rfq() =
        RetrofitInstance.ClientApi!!.rfqData()

     fun rfq1() =
        RetrofitInstance.ClientApi!!.rfqData1()

    suspend fun rfqDetails(orderno:String) =
        RetrofitInstance.ClientApi!!.rfqDeatilsData(orderno)



    suspend fun rfqDeatilsForCustomerData(orderno:String) =
        RetrofitInstance.ClientApi!!.rfqDeatilsForCustomerData(orderno)

     fun rfqDetails1(orderno:String) =
        RetrofitInstance.ClientApi!!.rfqDeatilsData1(orderno)



    suspend fun getPictures() = RetrofitInstance.otherClientApi!!.getPictures()

    suspend fun getAllProducts(shopId: Int, subProductId: Int, MyProductOnly: Int, page: Int) =
        RetrofitInstance.ClientApi!!.allProducts(shopId, subProductId)

    suspend fun getAllProducts(shopId: Int, page: Int,myProductOnly:Int) =
        RetrofitInstance.ClientApi!!.allProducts1(shopId)

    suspend fun updateProducts(
        productId: Int,
        qty: String,
        priceType: String,
        price: String,
        shopId: Int
    ) =
        RetrofitInstance.ClientApi!!.updateProducts(productId, qty, priceType, price, shopId)

    suspend fun removeProducts(
        productId: Int,
        shopId: Int
    ) =
        RetrofitInstance.ClientApi!!.removeProducts(productId, shopId)



    suspend fun bidUpdate(
        productId: Int,
        shopId: Int
    ) =
        RetrofitInstance.ClientApi!!.biding(productId, shopId)


    suspend fun createNewShop(requestBody: RequestBody) =
        RetrofitInstance.ClientApi!!.createNewShop(requestBody)

    suspend fun customerNotification() =
        RetrofitInstance.ClientApi!!.getCustomerNotification()
    suspend fun customerNotificationRead() =
        RetrofitInstance.ClientApi!!.customerNotificationRead()

    suspend fun sellerNotification() =
        RetrofitInstance.ClientApi!!.getSellerNotification()
    suspend fun sellerNotificationRead() =
        RetrofitInstance.ClientApi!!.sellerNotificationRead()

    suspend fun acceptRejectQuotation(orderId: String, accepted: Int) =
        RetrofitInstance.ClientApi!!.acceptRejectQuotation(orderId, accepted)

    suspend fun customerChatCount() =
        RetrofitInstance.ClientApi!!.getCustomerMessageCount()
    suspend fun sellerChatCount() =
        RetrofitInstance.ClientApi!!.getSellerMessageAndQuotationCount()

    suspend fun distanceMatrixGoogleApi(origins:String,destinations:String,mode:String,language: String,key:String) =
        RetrofitInstance.mapClientApi!!.distanceMatrixGoogleApi(origins,destinations,mode,language,key )

}