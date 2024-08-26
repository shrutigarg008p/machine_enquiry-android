package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 3/10/2022.
 */
data class AddressListResponse(
    override val status: Int,
    override val message: String,
    var data: ArrayList<AddressDTO>
) : CommonDTO

data class AddressDTO(

    @SerializedName("id") var id: Long,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("address_1") var address1: String,
    @SerializedName("address_2") var address2: String,
    @SerializedName("city") var city: String,
    @SerializedName("state") var state: String,
    @SerializedName("zip") var zip: String,
    @SerializedName("country") var country: String,
    @SerializedName("address") var address: String,
    @SerializedName("is_primary") var isPrimary: Boolean,
    @SerializedName("latitude") var latitude: String,
    @SerializedName("longitude") var longitude: String

)




