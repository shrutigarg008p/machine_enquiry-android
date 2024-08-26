package com.machine.machine.network

import java.io.File

object RequestBodies {


    data class LoginBody(
        val emailPhone: String,
        val password: String,
        val deviceToken:String
    )

    data class RegisterOtp(
        val type: String,
        val emailPhone: String
    )

    data class OtpVerfiy(
        val otp: String,
        val emailPhone: String
    )

    data class forgetPassword(
        val otp: String,
        val emailPhone: String,
        val password: String,
        val confirmPassword: String
    )

    data class changePassword(

        val oldPassword: String,
        val password: String,
        val confirmPassword: String
    )

    data class helpAndSupport(

        val name: String,
        val email: String,
        val msg: String
    )

    data class Address(
        var name: String,
        var email: String,
        var phone: String,
        var address1: String,
        var address2: String,
        var city: String,
        var state: String,
        var zip: String,
        var country: String,
        var latitude: String,
        var longitude: String
    )

    data class submitQuotation(
        var address_id: String? = null,
        var delivery_type: String? = null,
        var items: ArrayList<Items> = arrayListOf()
    )

    data class Items(
        var product_id: String? = null,
        var bid: String? = null

    )

    data class editProfile(
        val name: String,
        val profile_pic: File

    )

}