package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 4/5/2022.
 */
data class ProductListResponse(
    override val status: Int,
    override val message: String,
    var data: ArrayList<ProductData> = arrayListOf()
) : CommonDTO

data class ProductData(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("short_description") var shortDescription: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("is_favourite") var isFavourite: Boolean? = null

)