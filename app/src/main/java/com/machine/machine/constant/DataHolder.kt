package com.machine.machine.constant

import com.machine.machine.model.common.DataField

object DataHolder {

    fun getUserType(): ArrayList<DataField> {
        val objItem = ArrayList<DataField>()
        objItem.add(DataField(id = "customer", value = "Customer"))
        objItem.add(DataField(id = "seller", value = "Seller"))
        return objItem;
    }
}