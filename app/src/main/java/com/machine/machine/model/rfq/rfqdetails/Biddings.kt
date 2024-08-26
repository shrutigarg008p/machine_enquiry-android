package com.example.example

import com.google.gson.annotations.SerializedName


data class Biddings (

  @SerializedName("id"               ) var id             : Int?     = null,
  @SerializedName("customer"         ) var customer       : String?  = null,
  @SerializedName("seller"           ) var seller         : String?  = null,
  @SerializedName("bid"              ) var bid            : String?  = null,
  @SerializedName("action"           ) var action         : String?  = null,
  @SerializedName("open_for_bidding" ) var openForBidding : Boolean? = null

)