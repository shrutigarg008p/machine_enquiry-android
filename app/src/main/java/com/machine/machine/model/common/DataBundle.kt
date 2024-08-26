package com.machine.machine.model.common

import java.io.Serializable

/**
 * Created by Amit Rawat on 4/7/2022.
 */

data class DataBundle(
    var shopId: Long? = null,
    var catId: Long? = null,
    var subCatId: Long? = null,
    var title: String? = null
) : Serializable