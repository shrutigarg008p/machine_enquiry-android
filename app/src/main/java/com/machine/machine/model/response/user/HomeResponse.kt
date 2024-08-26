package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 3/31/2022.
 */
data class HomeResponse(
    override val status: Int,
    override val message: String,
    val data: HomeData?

) : CommonDTO


data class HomeData(
    val banners: ArrayList<Banner>,
    val categories: ArrayList<HomeCategory>,

    @SerializedName("favourite_shops")
    val favouriteShops: ArrayList<FavouriteShop>,

    @SerializedName("cart_count")
    val cartCount: Int

)


data class Banner(
    val id: Long,
    val title: String,
    val image: String
)

data class HomeCategory(
    val id: Long,
    val title: String,

    @SerializedName("cover_image")
    val coverImage: String?
)

data class FavouriteShop(
    val id: Long,

    @SerializedName("shop_image")
    val shopImage: String,

    @SerializedName("shop_name")
    val shopName: String?,

    )