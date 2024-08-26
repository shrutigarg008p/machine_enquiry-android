import com.google.gson.annotations.SerializedName


data class ChatResponseModel1(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)