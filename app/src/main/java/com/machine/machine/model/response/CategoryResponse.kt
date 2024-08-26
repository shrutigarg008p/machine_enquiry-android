package com.machine.machine.model.response

import com.machine.machine.model.CommonDTO

data class CategoryResponse(
    override val status: Int,
    override val message: String,
    var data: ArrayList<CategoryDto> = arrayListOf()
) : CommonDTO


data class CategoryDto(
    var id: Long? = null,
    var title: String? = null,

    )

