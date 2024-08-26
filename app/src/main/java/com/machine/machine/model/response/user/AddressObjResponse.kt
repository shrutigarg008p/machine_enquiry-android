package com.machine.machine.model.response.user

import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 3/10/2022.
 */
data class AddressObjResponse(
    override val status: Int,
    override val message: String,
    var data: AddressDTO
) : CommonDTO





