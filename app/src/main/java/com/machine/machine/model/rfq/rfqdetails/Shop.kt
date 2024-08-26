package com.machine.machine

import com.google.gson.annotations.SerializedName


data class Shop (

  @SerializedName("id"         ) var id        : Int?    = null,
  @SerializedName("shop_name"  ) var shopName  : String? = null,
  @SerializedName("shop_email" ) var shopEmail : String? = null

)