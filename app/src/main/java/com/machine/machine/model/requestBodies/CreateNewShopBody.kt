package com.machine.machine.model.requestBodies

import android.os.Parcelable
import com.machine.machine.model.PicItem
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class CreateNewShopBody(
    var shopOwner: String,
    var shopName: String,
    var shopEmail: String,
    var shopContact: String,
    var registrationNo: String,
    var country: String,
    var state: String,
    var address_1: String,
    var address_2: String,
    var productCategories: @RawValue ArrayList<Int> = ArrayList(),
    var workingHoursFrom: String,
    var workingHoursTo: String,
    var workingDays: @RawValue ArrayList<String> = ArrayList(),
    var photos: @RawValue ArrayList<PicItem> = ArrayList(),
    var latitude: String,
    var longitude: String,
) : Parcelable
