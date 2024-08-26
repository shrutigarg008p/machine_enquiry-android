package com.machine.machine.model.requestBodies

import android.os.Parcelable
import com.machine.machine.model.PicItem
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class RegisterBody(
    var name: String? = null,
    var email: String? = null,
    var profile_pic: String? = null,
    var phone_code: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var password_confirmation: String? = null,
    var shop_name: String? = null,
    var shopOwnerName: String? = null,
    var shop_email: String? = null,
    var shop_number: String? = null,
    var registration_no: String? = null,
    var category: @RawValue ArrayList<Int> = ArrayList(),
    var country: String? = null,
    var state: String? = null,
    var address_1: String? = null,
    var address_2: String? = null,
    var working_hours_from: String? = null,
    var working_hours_to: String? = null,
    var working_days: String? = null,
    var emailPhone: String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var workingDayList: @RawValue ArrayList<String> = ArrayList(),
    var shop_imagePath: @RawValue ArrayList<PicItem> = ArrayList(),

    ) : Parcelable

