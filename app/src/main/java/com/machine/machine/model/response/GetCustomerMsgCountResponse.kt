package com.machine.machine.model.response

import com.google.gson.annotations.SerializedName

data class GetCustomerMsgCountResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : GetCustomerMsgCountData?   = GetCustomerMsgCountData()

)

data class GetCustomerMsgCountData(
    @SerializedName("new_message_count" ) var newMessageCount : Int? = null
)
