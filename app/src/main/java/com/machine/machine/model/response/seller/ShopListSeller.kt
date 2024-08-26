package com.example.example

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO


data class ShopListSeller (

  override var status  : Int,
  override  var message : String,
  @SerializedName("data") var data    : ArrayList<Data> = arrayListOf()

) : CommonDTO