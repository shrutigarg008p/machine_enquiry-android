package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 4/8/2022.
 */
class ProductDetailResponse(
    override val status: Int,
    override val message: String,
    var data: ProductDetail
) : CommonDTO

data class ProductDetail(

    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("short_description") var shortDescription: String,
    @SerializedName("price") var price: String,
    @SerializedName("currency") var currency: String,
    @SerializedName("price_type") var priceType: String,
    @SerializedName("image") var image: String,
    @SerializedName("addtional_images") var additionalImage: ArrayList<String>,
    @SerializedName("seller_id") var sellerId: Long? = null,
    @SerializedName("description") var description: String,
    @SerializedName("additional_info") var additionalInfo: ArrayList<AdditionalInfo>,
    @SerializedName("is_favourite") var isFavourite: Boolean,
    @SerializedName("in_cart") var isAddCart: Boolean

)

data class AdditionalInfo(
    @SerializedName("key") var key: String,
    @SerializedName("value") var value: String
)