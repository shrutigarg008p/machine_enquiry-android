package com.machine.machine.model

import com.google.gson.annotations.SerializedName

data class UserDTO(
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var status: Boolean? = null,
    var onboarded: Boolean? = null,
    val type: String? = null,
    @SerializedName("email_verified") val emailVerified: Boolean? = null,
    @SerializedName("phone_verified") val phoneVerified: Boolean? = null,
    @SerializedName("profile_pic") val profilePic: String? = null,
    @SerializedName("notification_count") val notificationCount: Int? = null,

    val settings: Settings? = null
)

data class Settings(
    val language: Language,

    @SerializedName("allow_notification")
    val allowNotification: Boolean
)

data class Language(
    val locale: String,
    val direction: String,
    val title: String
)