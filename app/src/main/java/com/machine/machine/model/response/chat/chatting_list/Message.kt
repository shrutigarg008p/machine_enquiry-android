
import com.google.gson.annotations.SerializedName


data class Message(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("text") var text: String? = null,
    @SerializedName("is_mine") var isMine: Boolean? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("created_at_str") var createdAtStr: String? = null,
    @SerializedName("type") var type: String? = null


)