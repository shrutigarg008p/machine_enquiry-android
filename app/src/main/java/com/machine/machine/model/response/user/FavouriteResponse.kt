package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 3/31/2022.
 */
data class FavouriteResponse(
    override val status: Int,
    override val message: String,
    val data: ArrayList<ShopDto>?

) : CommonDTO

data class ShopDto(
    val id: Long,

    @SerializedName("shop_owner")
    val shopOwner: String,

    @SerializedName("shop_name")
    val shopName: String,

    @SerializedName("shop_contact")
    val shopContact: String,

    @SerializedName("shop_email")
    val shopEmail: String,

    @SerializedName("shop_logo")
    val shopLogo: String,

    val distance: String,

    @SerializedName("distance_unit")
    val distanceUnit: String,


    val rating: String,
    val offer: String,

    @SerializedName("order_timing")
    val orderTiming: String,

    @SerializedName("registration_no")
    val registrationNo: String,

    @SerializedName("added_to_favourite")
    var isFavourite: Boolean,

    val address: String,

    @SerializedName("working_hours")
    val workingHours: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("working_days")
    val workingDays: List<String>,

    @SerializedName("images")
    val images: List<String>
)