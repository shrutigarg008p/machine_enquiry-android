package com.machine.machine.model.response

import com.google.gson.annotations.SerializedName

data class GetSellerMsgCountResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : GetSellerMsgCountData?   = GetSellerMsgCountData()
)

data class GetSellerMsgCountData(
    @SerializedName("new_message_count" ) var newMessageCount : Int? = null,
    @SerializedName("new_quotation"     ) var newQuotation    : Int? = null
)
