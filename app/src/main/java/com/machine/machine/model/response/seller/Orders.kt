package com.example.example

import com.google.gson.annotations.SerializedName


data class Orders (

  @SerializedName("new"         ) var new        : String? = null,
  @SerializedName("in_progress" ) var inProgress : String? = null,
  @SerializedName("closed"      ) var closed     : String? = null

)