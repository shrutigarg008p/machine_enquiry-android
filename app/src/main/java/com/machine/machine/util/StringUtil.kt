package com.machine.machine.util

import com.machine.machine.constant.BaseConstants

object StringUtil {

    fun getString(value: String?): String {
        if (value != null) {
            return value
        }
        return BaseConstants.EMPTY
    }

    fun getInt(value: Int?): Int {
        if (value != null) {
            return value
        }
        return 0
    }
}