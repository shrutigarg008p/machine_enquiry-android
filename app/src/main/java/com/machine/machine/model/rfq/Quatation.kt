package com.machine.machine

import com.google.gson.annotations.SerializedName


data class Quatation (

  @SerializedName("status"  ) var status  : Int?            = null,
  @SerializedName("message" ) var message : String?         = null,
  @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()

)