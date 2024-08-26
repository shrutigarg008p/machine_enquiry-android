
import com.google.gson.annotations.SerializedName


data class Messages(

    @SerializedName("message") var message: Message? = Message(),
    @SerializedName("user") var user: User? = User()

)