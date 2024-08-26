package com.machine.machine

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("order_no" ) var orderNo : String? = null,
  @SerializedName("date"     ) var date    : String? = null,
  @SerializedName("date_str" ) var dateStr : String? = null

)