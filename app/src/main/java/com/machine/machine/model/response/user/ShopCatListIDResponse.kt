package com.machine.machine.model.response.user

import com.machine.machine.model.CommonDTO


data class ShopCatListIDResponse(
    override val status: Int,
    override val message: String,
    var data: ArrayList<ShopDto> = arrayListOf(),
) : CommonDTO

