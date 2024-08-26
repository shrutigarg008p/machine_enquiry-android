package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

class UserNotificationResponse (
    override val status: Int,
    override val message: String,
    var data: ArrayList<NotificationData> = arrayListOf()
) : CommonDTO

data class NotificationDataObj (

    @SerializedName("count" ) var count : Int?  = null,
    @SerializedName("data"  ) var data  : ArrayList<NotificationData> = arrayListOf()

)

data class NotificationData(
    @SerializedName("title"       ) var title : String? = null,
    @SerializedName("body"        ) var body  : String? = null,
    @SerializedName("status"      ) var status : String? = null,
    @SerializedName("created_at"  ) var createdAt : String? = null
)