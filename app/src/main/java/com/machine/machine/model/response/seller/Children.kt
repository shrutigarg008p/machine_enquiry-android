package com.example.example

import com.google.gson.annotations.SerializedName


data class Children (

  @SerializedName("id"                ) var id               : Int?    = null,
  @SerializedName("title"             ) var title            : String? = null,
  @SerializedName("short_description" ) var shortDescription : String? = null,
  @SerializedName("description"       ) var description      : String? = null,
  @SerializedName("cover_image"       ) var coverImage       : String? = null

)