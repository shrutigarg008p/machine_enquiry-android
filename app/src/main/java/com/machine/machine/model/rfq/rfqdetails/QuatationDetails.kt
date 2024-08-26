package com.machine.machine

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.rfq.Data


data class QuatationDetails (

  @SerializedName("status"  ) var status  : Int?    = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("data"    ) var data    : Data?   = com.machine.machine.model.rfq.Data()

)