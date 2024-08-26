package com.machine.machine.model.response

import com.machine.machine.model.CommonDTO
import com.machine.machine.model.UserDTO

data class RegistrationResponse(
    override val status: Int,
    override val message: String,
    var data: UserDTO? = UserDTO()

) : CommonDTO

