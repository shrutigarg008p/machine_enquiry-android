import com.google.gson.annotations.SerializedName


data class AddChatResponse (

  @SerializedName("status"  ) var status  : Int?    = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("data"    ) var data    : Data1?   = Data1()

)