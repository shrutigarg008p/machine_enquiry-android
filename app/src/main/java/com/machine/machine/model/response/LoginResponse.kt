package com.machine.machine.model.response

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO
import com.machine.machine.model.UserDTO

data class LoginResponse(
    override val status: Int,
    override val message: String,
    var data: DataDto? = DataDto()

) : CommonDTO


data class DataDto(
    @SerializedName("access_token") var accessToken: String? = null,
    @SerializedName("token_type") var tokenType: String? = null,
    @SerializedName("expires_in") var expiresIn: Int? = null,
    @SerializedName("user") var user: UserDTO? = UserDTO()
)