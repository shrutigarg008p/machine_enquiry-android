package com.machine.machine

import com.example.example.Biddings
import com.google.gson.annotations.SerializedName


data class Items (

  @SerializedName("id"                ) var id               : Int?              = null,
  @SerializedName("title"             ) var title            : String?           = null,
  @SerializedName("short_description" ) var shortDescription : String?           = null,
  @SerializedName("price"             ) var price            : String?           = null,
  @SerializedName("currency"          ) var currency         : String?           = null,
  @SerializedName("price_type"        ) var priceType        : String?           = null,
  @SerializedName("image"             ) var image            : String?           = null,
  @SerializedName("seller"            ) var seller           : String?           = null,
  @SerializedName("quantity"          ) var quantity         : Int?              = null,
  @SerializedName("total_amount"      ) var totalAmount      : String?           = null,
  @SerializedName("shop"              ) var shop             : Shop?             = Shop(),
  @SerializedName("biddings"          ) var biddings         : ArrayList<Biddings> = arrayListOf(),
  @SerializedName("accepted_bid"      ) var acceptedBid      : String?           = null

)