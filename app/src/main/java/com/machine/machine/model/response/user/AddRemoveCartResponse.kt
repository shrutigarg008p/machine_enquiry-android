package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 4/13/2022.
 */
data class AddRemoveCartResponse(
    override val status: Int,
    override val message: String,
    var data: ItemObj

) : CommonDTO

data class ItemObj(

    @SerializedName("item") var item: ItemDetail,
    @SerializedName("product_qty") var productQty: Int,
    @SerializedName("cart_items") var cartItems: Int,
    @SerializedName("currency") var currency: String,
    @SerializedName("total_sum") var totalSum: String

)


data class ItemDetail(

    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("short_description") var shortDescription: String,
    @SerializedName("price") var price: String,
    @SerializedName("currency") var currency: String,
    @SerializedName("price_type") var priceType: String,
    @SerializedName("image") var image: String,
    @SerializedName("seller") var seller: String,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("total_amount") var totalAmount: String

)

