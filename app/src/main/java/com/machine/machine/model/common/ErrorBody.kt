package com.machine.machine.model.common

import com.google.gson.annotations.SerializedName

class ErrorBody {
  @SerializedName("message")
  var message: String? = null

  @SerializedName("data")
  var data: String? = null
}
