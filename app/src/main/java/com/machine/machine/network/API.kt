package com.machine.machine.network

import AddChatResponse
import ChatResponseModel1
import CreateShopResponse
import com.example.example.ShopListSeller
import com.machine.machine.Quatation
import com.machine.machine.QuatationDetails
import com.machine.machine.constant.ApiConst
import com.machine.machine.constant.FieldConst
import com.machine.machine.model.CommonResponse
import com.machine.machine.model.PicsResponse
import com.machine.machine.model.response.*
import com.machine.machine.model.response.chat.ChatResponseModel
import com.machine.machine.model.response.seller.AllProducts
import com.machine.machine.model.response.user.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface API {

    @GET(ApiConst.GETPIC)
    suspend fun getPictures(): Response<PicsResponse>

    /*AUTH */
    @FormUrlEncoded
    @POST(ApiConst.LOGIN)
    suspend fun loginUser(
        @Field(FieldConst.EMAIL_OR_PHONE) emailPhone: String,
        @Field(FieldConst.PASSWORD) password: String,
        @Field(FieldConst.Device_Token) deviceToken :String
    ): Response<LoginResponse>


    @FormUrlEncoded
    @POST(ApiConst.REGISTER_OTP)
    suspend fun registerOTP(
        @Field(FieldConst.TYPE) type: String,
        @Field(FieldConst.EMAIL_OR_PHONE) emailPhone: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST(ApiConst.OTP_VERIFY)
    suspend fun otpVerify(
        @Field(FieldConst.OTP) type: String,
        @Field(FieldConst.EMAIL_OR_PHONE) emailPhone: String
    ): Response<LoginResponse>


    @FormUrlEncoded
    @POST(ApiConst.FORGET_PASSWORD)
    suspend fun forgetPassword(
        @Field(FieldConst.OTP) type: String,
        @Field(FieldConst.EMAIL_OR_PHONE) emailPhone: String,
        @Field(FieldConst.PASSWORD) password: String,
        @Field(FieldConst.CONFIRM_PASSWORD) confirmPassword: String
    ): Response<CommonResponse>


    /*CLIENT */

    @POST(ApiConst.REGISTER)
    suspend fun register(@Body file: RequestBody): Response<RegistrationResponse>

    @FormUrlEncoded
    @POST(ApiConst.CATEGORY_LIST)
    suspend fun categoryListRegister(@Field(FieldConst.PARENT_ONLY) category: Long): Response<CategoryResponse>

    @FormUrlEncoded
    @POST(ApiConst.SUB_CATEGORY_LIST)
    suspend fun subCategoryList(
        @Field(FieldConst.CATEGORY) category: Long
    ): Response<CategoryResponse>

    @POST(ApiConst.EDIT_PROFILE)
    suspend fun editProfile(
        @Body file: RequestBody
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST(ApiConst.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Field(FieldConst.OLD_PASSWORD) oldPassword: String,
        @Field(FieldConst.PASSWORD) password: String,
        @Field(FieldConst.CONFIRM_PASSWORD) confirmPassword: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST(ApiConst.HELP_AND_SUPPORT)
    suspend fun helpAndSuppot(
        @Field(FieldConst.NAME) name: String,
        @Field(FieldConst.EMAIL) email: String,
        @Field(FieldConst.MESSAGE) message: String
    ): Response<CommonResponse>


    @FormUrlEncoded
    @POST(ApiConst.NOTIFICATION)
    suspend fun notification(
        @Field(FieldConst.NOTIFICATON) notification: String
    ): Response<CommonResponse>

    @GET(ApiConst.GET_HOME_DATA)
    suspend fun getHomeData(): Response<HomeResponse>

    @GET(ApiConst.GET_SHOP_FAVOURITE_LIST)
    suspend fun getShopFavList(): Response<FavouriteResponse>


    @GET(ApiConst.GET_PRODUCT_FAVOURITE_LIST)
    suspend fun getProductFavList(): Response<ProductListResponse>


    @GET(ApiConst.GET_SETTING_ME)
    suspend fun getSettingData(): Response<SettingResponse>

    @GET(ApiConst.GET_CHAT_LIST)
    suspend fun getChatList(): Response<ChatResponseModel>

    @GET(ApiConst.GET_CHATS + "/{id}")
    suspend fun getChats(@Path("id") id: Int): Response<ChatResponseModel1>

    @FormUrlEncoded
    @POST(ApiConst.SEND_MESSAGES + "/{id}")
    suspend fun sendMessage(
        @Field("message") message: String,
        @Path("id") id: Int,
        @Field("type") type: Int,

        ): Response<CommonResponse>


    @FormUrlEncoded
    @POST(ApiConst.CATEGORY_SHOP_LISTING_BY_ID)
    suspend fun getCategoryShopListById(
       // @Field(FieldConst.PAGE) page: Int,
        @Field(FieldConst.CATEGORY) category: Long
    ): Response<ShopCatListIDResponse>

    @FormUrlEncoded
    @POST(ApiConst.ADD_SHOP_TO_FAV)
    suspend fun addShopToFavourite(
        @Field(FieldConst.SHOP) category: Long
    ): Response<AddFavouriteResponse>


    @FormUrlEncoded
    @POST(ApiConst.GET_SHOP_DETAIL)
    suspend fun getShopDetail(
        @Field(FieldConst.SHOP) shopId: Long
    ): Response<ShopDetailResponse>

    @FormUrlEncoded
    @POST(ApiConst.Create_Chat)
    suspend fun getCreateChat(@Field(FieldConst.users) sellerId: List<Int>
    ): Response<AddChatResponse>

    @FormUrlEncoded
    @POST(ApiConst.Create_Chat)
     fun getCreatenewChat(@Field(FieldConst.users) sellerId: List<Int>
    ): Response<AddChatResponse>


    @FormUrlEncoded
    @POST(ApiConst.GET_PRODUCT_LIST_BY_CAT_ID)
    suspend fun getProductListById(
        @Field(FieldConst.PAGE) page: Int,
        @Field(FieldConst.CATEGORY) subCategoryId: Long,
        @Field(FieldConst.SHOP) shopId: Long
    ): Response<ProductListResponse>


    @FormUrlEncoded
    @POST(ApiConst.ADD_PRODUCT_TO_FAV)
    suspend fun addProductToFavourite(
        @Field(FieldConst.PRODUCT) product: Long
    ): Response<AddFavouriteResponse>

    @FormUrlEncoded
    @POST(ApiConst.GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(
        @Field(FieldConst.PRODUCT) product: Long
    ): Response<ProductDetailResponse>


    @POST(ApiConst.LOGOUT)
    suspend fun logout(): Response<CommonResponse>


    @GET(ApiConst.GET_ALL_CART_ITEM_LISTING)
    suspend fun getAllCartItem(): Response<CartListingResponse>


    @FormUrlEncoded
    @POST(ApiConst.ADD_TO_CART_ITEM)
    suspend fun addCartItem(
        @Field(FieldConst.PRODUCT) product: Long,
        @Field(FieldConst.QUANTITY) quantity: Long
    ): Response<AddRemoveCartResponse>


    /*QUANITITY -1 REMOVING THE ENTIRE ITEM*/
    @FormUrlEncoded
    @POST(ApiConst.REMOVE_CART_ITEM)
    suspend fun removeCardItem(
        @Field(FieldConst.PRODUCT) product: Long,
        @Field(FieldConst.QUANTITY) quantity: Long
    ): Response<AddRemoveCartResponse>


    @POST(ApiConst.SUBMIT_QUOTATION)
    suspend fun sendQuotationsApi(
        @Body body: RequestBodies.submitQuotation
    ): Response<CommonResponse>

    @GET(ApiConst.GET_ALL_ADDRESS)
    suspend fun getAllAddress(): Response<AddressListResponse>


    @FormUrlEncoded
    @POST(ApiConst.SET_AS_PRIMARY_ADDRESS)
    suspend fun setAsPrimaryAddress(
        @Field(FieldConst.ADDRESS_ID) addressId: Long,
        @Field(FieldConst.IS_PRIMARY) isPrimary: Long
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST(ApiConst.DELETE_ADDRESS)
    suspend fun deleteAddress(
        @Field(FieldConst.ADDRESS_ID) addressId: Long,
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST(ApiConst.GET_ADDRESS_BY_ID)
    suspend fun getAddressById(
        @Field(FieldConst.ADDRESS_ID) addressId: Long,
    ): Response<AddressObjResponse>


    @POST(ApiConst.ADD_NEW_ADDRESS)
    suspend fun addAddress(@Body file: RequestBody): Response<CommonResponse>

    @POST(ApiConst.UPDATE_ADDRESS)
    suspend fun updateAddress(@Body file: RequestBody): Response<CommonResponse>

    @GET(ApiConst.GET_SELLER_SHOPS)
    suspend fun sellerShops(): Response<ShopListSeller>

    @GET(ApiConst.GET_SELLER_RFQ)
    suspend fun rfqData(): Response<Quatation>

    @GET(ApiConst.GET_SELLER_RFQ)
     fun rfqData1(): Call<Quatation>
   
    @FormUrlEncoded
    @POST(ApiConst.GET_SELLER_DETAILS_RFQ)
    suspend fun rfqDeatilsData(@Field("order_no") orderId: String): Response<QuatationDetails>

    @FormUrlEncoded
    @POST(ApiConst.GET_CUSTOMER_DETAILS_RFQ)
    suspend fun rfqDeatilsForCustomerData(@Field("order_no") orderId: String): Response<QuatationDetails>

    @FormUrlEncoded
    @POST(ApiConst.GET_SELLER_DETAILS_RFQ)
     fun rfqDeatilsData1(@Field("order_no") orderId: String): Call<QuatationDetails>


    @FormUrlEncoded
    @POST(ApiConst.ALL_PRODUCTS)
    suspend fun allProducts(
        @Field("shop_id") shopId: Int, @Field("sub_category_id") subCategoryId: Int,
        //@Field("my_products_only") myProductsOnly: Int,
      //  @Query("page") page: Int
    ): Response<AllProducts>

    @FormUrlEncoded
    @POST(ApiConst.ALL_PRODUCTS)
    suspend fun allProducts1(
        @Field("shop_id") shopId: Int,
       // @Query("page") page: Int,
       // @Field("my_products_only") myProductsOnly: Int
    ): Response<AllProducts>

    @FormUrlEncoded
    @POST(ApiConst.UPDATE_PRODUCT)
    suspend fun updateProducts(
        @Field("product_id") productId: Int,
        @Field("qty") qty: String,
        @Field("price_type") priceType: String,
        @Field("price") price: String,
        @Field("shop_id") shopId: Int,
    ): Response<CommonResponse>

    @POST(ApiConst.CREATE_NEW_SHOP)
    suspend fun createNewShop(
        @Body file: RequestBody
    ): Response<CreateShopResponse>


    @FormUrlEncoded
    @POST(ApiConst.REMOVE_PRODUCT)
    suspend fun removeProducts(
        @Field("product_id") productId: Int,
        @Field("shop_id") shopId: Int,
    ): Response<CommonResponse>


    @FormUrlEncoded
    @POST(ApiConst.BID_UPDATE)
    suspend fun biding(
        @Field("bid_id") bidId: Int,
        @Field("accepted") status: Int,
    ): Response<CommonResponse>

    @GET(ApiConst.customerNotification)
    suspend fun getCustomerNotification(): Response<UserNotificationResponse>

    @GET(ApiConst.customerNotificationRead)
    suspend fun customerNotificationRead(): Response<UserNotificationResponse>

    @GET(ApiConst.sellerNotification)
    suspend fun getSellerNotification(): Response<UserNotificationResponse>

    @GET(ApiConst.sellerNotificationRead)
    suspend fun sellerNotificationRead(): Response<UserNotificationResponse>

    @FormUrlEncoded
    @POST(ApiConst.acceptRejectQuotation)
    suspend fun acceptRejectQuotation(
        @Field("id") orderId: String,
        @Field("accepted") status: Int,
    ): Response<CommonResponse>

    @GET(ApiConst.getCustomerMessageCount)
    suspend fun getCustomerMessageCount(): Response<GetCustomerMsgCountResponse>

    @GET(ApiConst.getSellerMessageAndQuotationCount)
    suspend fun getSellerMessageAndQuotationCount(): Response<GetSellerMsgCountResponse>

    @GET(ApiConst.distanceMatrixGoogleApi)
    suspend fun distanceMatrixGoogleApi(@Query("origins") origins: String,
                                        @Query("destinations") destinations: String,
                                        @Query("mode") mode: String,
                                        @Query("language") language: String,
                                        @Query("key") key: String): Response<DistanceMatrixResponse>


    @GET(ApiConst.distanceMatrixGoogleApi)
     fun distanceMatrixGoogleApiwihtout(@Query("origins") origins: String,
                                        @Query("destinations") destinations: String,
                                        @Query("mode") mode: String,
                                        @Query("language") language: String,
                                        @Query("key") key: String): Call<DistanceMatrixResponse>

}