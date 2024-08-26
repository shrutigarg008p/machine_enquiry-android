package com.machine.machine.model.requestBodies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class
RegisterPageIntent(
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var type: String? = null,
    var latitiude: Double? = null,
    var longitude: Double? = null,
) : Parcelable

