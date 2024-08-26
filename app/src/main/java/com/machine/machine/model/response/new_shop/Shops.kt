import com.google.gson.annotations.SerializedName


data class Shops(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("shop_name") var shopName: String? = null

)