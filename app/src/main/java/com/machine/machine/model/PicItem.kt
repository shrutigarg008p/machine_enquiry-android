package com.machine.machine.model

import java.io.File

data class PicItem(
    var pic_path: File? = null,
    var delete: Boolean? = null,
)