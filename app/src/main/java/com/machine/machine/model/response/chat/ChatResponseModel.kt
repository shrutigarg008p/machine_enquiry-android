package com.machine.machine.model.response.chat

import com.google.gson.annotations.SerializedName

data class ChatResponseModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ChatListData? = ChatListData()
)
