
import com.google.gson.annotations.SerializedName


data class Data(

    @SerializedName("participants") var participants: String? = null,
    @SerializedName("messages") var messages: ArrayList<Messages> = arrayListOf()

)