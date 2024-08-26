import com.google.gson.annotations.SerializedName


data class Categories(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("short_description") var shortDescription: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("cover_image") var coverImage: String? = null,
    @SerializedName("shops") var shops: ArrayList<Shops> = arrayListOf()

)