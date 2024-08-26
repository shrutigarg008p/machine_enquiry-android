package com.machine.machine.model.rfq

import com.google.gson.annotations.SerializedName
import com.machine.machine.Items


data class Data (

  @SerializedName("order_no" ) var orderNo : String?          = null,
  @SerializedName("date"     ) var date    : String?          = null,
  @SerializedName("date_str" ) var dateStr : String?          = null,
  @SerializedName("items"    ) var items   : ArrayList<Items> = arrayListOf()

)