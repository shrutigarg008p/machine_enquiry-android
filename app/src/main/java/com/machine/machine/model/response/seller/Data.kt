package com.example.example

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("id"              ) var id             : Int?                  = null,
  @SerializedName("shop_owner"      ) var shopOwner      : String?               = null,
  @SerializedName("shop_name"       ) var shopName       : String?               = null,
  @SerializedName("shop_contact"    ) var shopContact    : String?               = null,
  @SerializedName("shop_email"      ) var shopEmail      : String?               = null,
  @SerializedName("shop_logo"       ) var shopLogo       : String?               = null,
  @SerializedName("distance"        ) var distance       : String?               = null,
  @SerializedName("distance_unit"   ) var distanceUnit   : String?               = null,
  @SerializedName("rating"          ) var rating         : String?               = null,
  @SerializedName("offer"           ) var offer          : String?               = null,
  @SerializedName("order_timing"    ) var orderTiming    : String?               = null,
  @SerializedName("registration_no" ) var registrationNo : String?               = null,
  @SerializedName("address"         ) var address        : String?               = null,
  @SerializedName("working_hours"   ) var workingHours   : String?               = null,
  @SerializedName("working_days"    ) var workingDays    : ArrayList<String>     = arrayListOf(),
  @SerializedName("additional"      ) var additional     : Additional?           = Additional(),
  @SerializedName("categories_shop" ) var categories     : ArrayList<Categories> = arrayListOf(),
  @SerializedName("images"          ) var images         : ArrayList<String>     = arrayListOf()

)