package com.machine.machine.model.common

import com.machine.machine.constant.BaseConstants

class DataField(val id: String?, val value: String?) {
    override fun toString(): String {
        if (value != null) {
            return value
        }
        return BaseConstants.EMPTY
    }
}