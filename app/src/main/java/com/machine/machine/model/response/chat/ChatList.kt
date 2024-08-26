package com.machine.machine.model.response.chat

import com.google.gson.annotations.SerializedName

data class ChatList(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("participants") var participants: String? = null,
    @SerializedName("unread_msg") var unread_msg: Int? = null,
    @SerializedName("last_msg") var last_msg: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("created_at_str") var createdAtStr: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("updated_at_str") var updatedAtStr: String? = null
)
