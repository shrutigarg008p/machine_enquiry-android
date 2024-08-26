import com.google.gson.annotations.SerializedName


data class CreateShopResponse(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: CreateShopData? = CreateShopData()

)