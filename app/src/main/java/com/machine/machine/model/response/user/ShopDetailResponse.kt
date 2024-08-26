package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 4/5/2022.
 */
data class ShopDetailResponse(
    override val status: Int,
    override val message: String,
    var data: DataObj? = DataObj()
) : CommonDTO


data class DataObj(

    @SerializedName("shop") var shop: ShopDetail? = ShopDetail(),
    @SerializedName("communication") var communication: Communication? = Communication(),
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("services") var services: ArrayList<String> = arrayListOf(),
    @SerializedName("ratings") var ratings: Ratings? = Ratings(),
    @SerializedName("categories") var categories: ArrayList<ProductCategories> = arrayListOf(),


)


data class Communication(

    @SerializedName("call") var call: String? = null,
    @SerializedName("whatsapp") var whatsapp: String? = null,
    @SerializedName("share") var share: Share? = Share()

)

data class ProductCategories(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("short_description") var shortDescription: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("cover_image") var coverImage: String? = null,

)

data class TotalByStar(

    @SerializedName("rating") var rating: Int? = null,
    @SerializedName("total") var total: Int? = null,
    @SerializedName("total_str") var totalStr: String? = null

)

data class ShopDetail(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("seller_id") var sellerId: Long? = null,
    @SerializedName("catId") var catId: Long? = null,
    @SerializedName("shop_owner") var shopOwner: String? = null,
    @SerializedName("shop_name") var shopName: String? = null,
    @SerializedName("shop_contact") var shopContact: String? = null,
    @SerializedName("shop_email") var shopEmail: String? = null,
    @SerializedName("shop_logo") var shopLogo: String? = null,
    @SerializedName("distance") var distance: String? = null,
    @SerializedName("distance_unit") var distanceUnit: String? = null,
    @SerializedName("rating") var rating: String? = null,
    @SerializedName("offer") var offer: String? = null,
    @SerializedName("order_timing") var orderTiming: String? = null,
    @SerializedName("registration_no") var registrationNo: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("working_hours") var workingHours: String? = null,
    @SerializedName("working_days") var workingDays: ArrayList<String> = arrayListOf(),
    @SerializedName("images") val images: List<String>?=null,
    @SerializedName("latitude") val latitude: Double=0.0,
    @SerializedName("longitude") val longitude: Double=0.0,

    @SerializedName("added_to_favourite") var isFavourite: Boolean? = null,
    @SerializedName("categories_shop") var categories_shop: ArrayList<ProductCategories> = arrayListOf()


)

data class Share(

    @SerializedName("facebook") var facebook: String? = null,
    @SerializedName("instagram") var instagram: String? = null

)

data class Ratings(

    @SerializedName("overall_average") var overallAverage: String? = null,
    @SerializedName("total") var total: Int? = null,
    @SerializedName("total_str") var totalStr: String? = null,
    @SerializedName("total_by_star") var totalByStar: ArrayList<TotalByStar> = arrayListOf(),
    @SerializedName("categories") var categories: RatingCategories? = RatingCategories(),
    @SerializedName("total_reviews") var totalReviews: Int? = null

)

data class RatingCategories(

    @SerializedName("service") var service: String? = null,
    @SerializedName("delivery") var delivery: String? = null,
    @SerializedName("quality") var quality: String? = null,
    @SerializedName("price") var price: String? = null

)

data class Parent(

    @SerializedName("id") var id: Long,
    @SerializedName("title") var title: String

)