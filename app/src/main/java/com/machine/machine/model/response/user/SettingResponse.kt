package com.machine.machine.model.response.user

import com.machine.machine.model.CommonDTO
import com.machine.machine.model.UserDTO

data class SettingResponse(
    override val status: Int,
    override val message: String,
    var data: UserDTO,
) : CommonDTO
