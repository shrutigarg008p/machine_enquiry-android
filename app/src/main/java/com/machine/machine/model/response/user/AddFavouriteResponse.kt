package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 3/10/2022.
 */
data class AddFavouriteResponse(
    override val status: Int,
    override val message: String,
    val data: Data?
) : CommonDTO

data class Data(
    @SerializedName("is_favourite")
    val isFavourite: Boolean,
)




