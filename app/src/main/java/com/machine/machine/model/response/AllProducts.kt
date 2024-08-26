package com.machine.machine.model.response.seller

import android.os.Parcelable
import com.machine.machine.model.CommonDTO
import kotlinx.android.parcel.Parcelize

data class AllProducts(
    override val status: Int,
    override val message: String,
    var data: ArrayList<ProductsDto> = arrayListOf()
) : CommonDTO

@Parcelize
data class ProductsDto(
    var id: Int? = null,
    var title: String? = null,
    var short_description: String? = null,
    var image: String? = null,
    var seller_product_id: Int? = null,
    var price: String? = "0",
    var currency: String? = null,
    var price_type: String? = null,
    var qty: Int? = 0
):Parcelable
